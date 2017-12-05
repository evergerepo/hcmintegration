package com.adp.smartconnect.oraclefusion.compgarn.pqq;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.PqqFileDetails;
import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;
import com.adp.smartconnect.oraclefusion.compgarn.integration.client.FileMover;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;
import com.adp.smartconnect.oraclefusion.compgarn.service.JobTrackingService;
import com.adp.smartconnect.oraclefusion.compgarn.util.CGConstants;
import com.adp.smartconnect.oraclefusion.compgarn.util.CommonUtil;
import com.adp.smartconnect.oraclefusion.compgarn.xmltypes.PQFile;
import com.adp.smartconnect.oraclefusion.compgarn.xmltypes.PreQualRecord;

public class PqqFileHandler {

	private static final Logger logger = LoggerFactory.getLogger(PqqFileHandler.class);

	private String clientName = null;

	private Configuration configuration;

	private ClientConfigHolder clientConfigurations;

	private PqqLoadTask pqqLoadTask = new PqqLoadTask();
	
	@Autowired  JobTrackingService jobTrackingService;
	
	

	/*
	 * Handle Input PQQ File
	 * 1. Create encoded UTF-8 file from source
	 * 2. Convert XML date to POJO
	 * 3. Reach each EMployee and Invoke Web Service
	 * 4. Get each Employee response and create response file
	 * 
	 */
	public File handleFile(File input) throws IOException {

		boolean isPartDone = false;
		boolean isError = false;
		boolean skipFile = false;
		File inProcessingFile = null;
		File doneFile = null;
		File partFile = null;
		File errorFile = null;
		String transId = CommonUtil.generateId();
		String jobStepId = CommonUtil.generateId(transId);
	
		try {
			String fileName = input.getName();
			MDC.put("transId", transId);
			MDC.put("fileName", fileName);
			logger.info("PQQ File processing STARTED. File Name: " + input.getAbsolutePath());

			// Check if the file is getting processed
			String inboundFileBaseName = FilenameUtils.getBaseName(fileName);
			String processingDir = configuration.getFileProcessingDir() + configuration.getPqqDir()+ configuration.getPqqProcessedDir();

			inProcessingFile = new File(processingDir + inboundFileBaseName + ".processing");
			doneFile = new File(processingDir + inboundFileBaseName + ".done");
			partFile = new File(processingDir + inboundFileBaseName + ".part.done");
			errorFile = new File(processingDir + inboundFileBaseName + ".error");

			if (inProcessingFile.exists() || doneFile.exists()) {
				logger.warn(">>>>>>>>>>>>>>>>>>>>>>PROCESSING/DONE file found, stop processing file.");
				skipFile = true;
				return null;
			}
			
			
			logger.info(">>>>>>>>>>>> Started Processing File.{}, TransId:{}", fileName, transId);
			
			// Create in processing file
			logger.info("Create Processing File: File Name:" + inProcessingFile.getAbsolutePath());
			inProcessingFile.createNewFile();

			processFile(input, transId, jobStepId) ;

		} catch (Exception exc) {
			logger.error("handleFile() Exception in PqqFileHandler. Message: "+exc.getMessage(), exc);
			errorFile.createNewFile();
			isError = true;
		} finally {
			if(skipFile==false){
				if (!isError && isPartDone) {
					partFile.createNewFile();
				} else if(!isError) {
					doneFile.createNewFile();
				}
				postProcess( input,  inProcessingFile);
			}
		}
		
		
		MDC.clear();
		logger.info("PQQ File processing COMPLETED. File Name: " + input.getAbsolutePath());
		return null;
	}
	
	
	/*
	 * UTF-8 File generation
	 */
	public File getUTF8File(File input) throws IOException {

		// Change file encoding to UTF-8 if not already. We should also move the
		// file to Temp - Start
		logger.debug("Start UTF-8 File creation");
		Path path = Paths.get(input.getAbsolutePath());
		byte[] encoded = Files.readAllBytes(path);
		String str = new String(encoded);
		String fileName = input.getName();
		String regex = "encoding=\"(.*)\"";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		String y = null;
		File utf8File = null;
		if (m.find()) {
			y = m.group(1);
			String s1 = str.replaceFirst(y, "UTF-8");
			utf8File = new File(configuration.getFileProcessingDir() + configuration.getPqqDir()
					+ configuration.getPqqArchiveDir() + fileName);
			FileUtils.writeStringToFile(utf8File, s1);
		}
		if (null == utf8File) {
			utf8File = input;
		}
		logger.info("UTF-8 File created. UTF-8 File Name: " + utf8File.getAbsolutePath());
		return utf8File;
	}

	/*
	 * File data to Object Un-marshalling 
	 */
	private PQFile convertFileToObject(File utf8File) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(PQFile.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		PQFile file = (PQFile) jaxbUnmarshaller.unmarshal(utf8File);
		return file;
	}

	
	/*
	 * Invoke Report Service and save response into reportLocation file
	 */
	private String callRunReport(PreQualRecord rec, PqqFileDetails fileDetails, String transID)
			throws Exception {

		Map<String, String> data = new HashMap<String, String>();
		String ssn = rec.getSSN();
		String firstName = rec.getEEFirstName();
		String lastName = rec.getEELastName();
		String docID = rec.getDocID();

		data.put("SSN", ssn);
		data.put("First Name", firstName);
		data.put("Last Name", lastName);
		data.put("DocId", docID);
		data.put("TransId", transID);
		
		pqqLoadTask.setUrl(fileDetails.getPqqUrl());
		pqqLoadTask.setLocale(fileDetails.getPqqLocale());
		pqqLoadTask.setChunkSizeDownload(-1);

		String response =  pqqLoadTask.callRunReport(fileDetails.getPqqReportPath(), data, fileDetails.getPqqUsername(),
				fileDetails.getPqqPassword(), fileDetails.getPqqFormat(), null);
		
		return response;
	}
	
	


	/*
	 * Merge PQQ Report Response
	 */
	private PQFile mergePQQReportResponse(List<String> reportResponse) throws JAXBException {
		JAXBContext j = JAXBContext.newInstance(PQFile.class);
		Unmarshaller u = j.createUnmarshaller();
		List<PreQualRecord> records = new ArrayList<>();
		PQFile que = null;
		for (String responseXml : reportResponse) {
			que = (PQFile) u.unmarshal(new StreamSource( new StringReader( responseXml.toString() ) ) );
			que.setTransmissionID(que.getTransId());
			que.setTransId(null);

			if (null != que.getPreQualRecordList() && null != que.getPreQualRecordList().getQualRecords()
					&& que.getPreQualRecordList().getQualRecords().size() != 0) {
				records.addAll(que.getPreQualRecordList().getQualRecords());
			}

		}

		que.getPreQualRecordList().setQualRecords(records);
		return que;
	}
	

	/*
	 * Create PQQ Response file
	 */
	private String createPQQResponseFile(ClientConfiguration config, PQFile que) throws JAXBException, IOException {

		String outputFile = null;
		try{
			// Create directory
			String pqqRespDir = configuration.getFileProcessingDir() + configuration.getPqqDir()+configuration.getPqqOutboundDir();
			String archiveDir = configuration.getFileProcessingDir() + configuration.getPqqDir()+configuration.getPqqArchiveDir();
			
			outputFile = getPQResponseFileName(config);
			logger.info("Pqq response File: {}" , outputFile);
	
			File file1 = new File(pqqRespDir + "/" + outputFile);
			JAXBContext jaxbContext1 = JAXBContext.newInstance(PQFile.class);
			Marshaller marshaller = jaxbContext1.createMarshaller();
			// To remove standalone yes from the generated xml
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			// marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml
			// version=\"1.0\" encoding=\"UTF-8\" ?>");
	
			// To add xsd
			marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "PQFile.xsd");
	
			java.io.FileWriter writer = new java.io.FileWriter(file1);
			writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			marshaller.marshal(que, writer);
		
			//Keep Response file as backup in Archive Folder
			FileMover.copyFile(pqqRespDir, archiveDir, outputFile);

			logger.info("PQQ Respons file created: File Name:{}", file1.getAbsolutePath());
		}catch(Exception e){
			logger.error("Error while creating PQQ Respons file:"+e.getMessage(), e);
			throw new IOException(e.getCause());
		}
		return outputFile;
	}

	
	
	
	/*
	 * Process PQQ Input File
	 */
	public boolean processFile(File input, String transId, String jobStepId) throws Exception{
		List<String> reportResponseList = new ArrayList<String>();
		boolean isPartDone = false;
		
		try{
			String fileName = input.getName();
			clientName = fileName.substring(0, 4).toLowerCase();
			logger.info("The client id in Pqq is :{}" , clientName);
			
			ClientConfiguration config = clientConfigurations.getSingleClientData(clientName);
			if(config==null){
				jobTrackingService.trackStartJob(CGConstants.JOB_PQQ_NAME, clientName, fileName, transId, jobStepId, CGConstants.JOB_STEP_PQQ_NAME);
				jobTrackingService.trackException(transId, jobStepId,  "ClientConfiguration", "ClientConfiguration set-up is missing. Client Id:"+clientName);
				new Exception("ClientConfiguration set-up is missing. Client Id:"+clientName);
			}
			
			//Create Job Event/Steps
			jobTrackingService.trackStartJob(config.getClientName(), clientName, fileName, transId, jobStepId, CGConstants.JOB_STEP_PQQ_NAME);
			
			// Get the UTF 8 File
			File utf8File = getUTF8File(input);//Check this is required TODO
			PQFile file = convertFileToObject(utf8File);
			
			List<PreQualRecord> qualRecords = file.getPreQualRecordList().getQualRecords();
			String transID = file.getTransmissionID();
			logger.info("No of PQQ records in file:"+file.getPreQualRecordList().getQualRecords().size());
			jobTrackingService.trackActivity(transId, jobStepId, "Records Count", "No of Records:"+qualRecords.size());
			
			int i = 1;
			Map<String, Boolean> successRecs = new HashMap<>();
			Map<String, Boolean> failureRecs = new HashMap<>();
	
			//Process each PQQ Record
			for (PreQualRecord rec : qualRecords) {
				String key = null;
				try {
					key = rec.getSSN() + " " + rec.getEEFirstName();
					jobTrackingService.trackActivity(transId, jobStepId, "Processing Record Number ["+i+"]", "Employee Name:"+rec.getEEFirstName()+" "+rec.getEELastName());
					String reportResponse = callRunReport(rec, config.getPqqFileDetails(), transID);
					reportResponseList.add(reportResponse);
					successRecs.put(key, true);
				} catch (Exception exc) {
					failureRecs.put(key, false);
					logger.error("Error processing PQQ Record. Record Key:"+key, exc);
					jobTrackingService.trackActivity(transId, jobStepId, "ERROR Processing PQQ Record", 
							"Employee Name:"+rec.getEEFirstName()+" "+rec.getEELastName()+", Message:"+exc.getMessage());
				}
	
				i = i + 1;
			}
			
			//Check for any Exceptions
			isPartDone = (failureRecs.size() != 0);
			if(isPartDone) {
				logger.error("No of Failed Records:"+failureRecs.size()+",  Sucessful records:"+successRecs.size());
				jobTrackingService.trackActivity(transId, jobStepId, "Processed Record Count", 
						"No of Failed Records:"+failureRecs.size()+",  Sucessful records:"+successRecs.size());
				
				Iterator<String> failRecsItr = failureRecs.keySet().iterator();
				while(failRecsItr.hasNext()) {
					String key = (String)failRecsItr.next();
					logger.error("Error Record Key:"+key);
				}
			}
			
			// Merge PQFile objects
			PQFile pqFile = mergePQQReportResponse(reportResponseList);
	
			// Create the response file
			String responseFile = createPQQResponseFile(config, pqFile);
			jobTrackingService.trackActivity(transId, jobStepId, "PQQ Response","PQQ Response File Generated. File Name:["+responseFile+"]");
		
		}catch(Exception exc){
			logger.error("handleFile() Exception in PqqFileHandler. Message: "+exc.getMessage(), exc);
			jobTrackingService.trackException(transId,  jobStepId, "PQQ Processing Error", exc.getMessage());
			throw exc;
		}
		return isPartDone;
	}
	
	/*
	 * Post process after completing the process
	 */
	private void postProcess(File input, File inProcessingFile){
		
		try{
			logger.info("PostProcess Started. Input File:{}, ProcessingFile:{}", input.getAbsolutePath(), inProcessingFile.getAbsolutePath());
			//1. Copy Input file to Archive Folder
			FileMover.handleFile(input, configuration.getFileProcessingDir()+configuration.getPqqDir()+configuration.getPqqArchiveDir());
			
			//2. Remove Input File
			FileMover.removeFile(input);
			
			//3. Remove In-Process Temp File
			inProcessingFile.delete();

		}catch(Exception e){
			logger.error("PQQ postProcess error. Message:"+e.getMessage(), e);
		}
		
	}
	
	
	/*
	 * Generate PQQ Response File
	 */
	private String getPQResponseFileName(ClientConfiguration config) {
		// Create Pqq Format
		String outputFormat = config.getPqqFileDetails().getPqqOutputFormat();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		java.util.Date now = calendar.getTime();
		String formattedDate = sdf.format(now);
		String outputFile = outputFormat.replaceAll("xxx", clientName).replaceAll("yyyymmddhhmmss", formattedDate);
		return outputFile;
	}


	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public ClientConfigHolder getClientConfigurations() {
		return clientConfigurations;
	}

	public void setClientConfigurations(ClientConfigHolder clientConfigurations) {
		this.clientConfigurations = clientConfigurations;
	}


}