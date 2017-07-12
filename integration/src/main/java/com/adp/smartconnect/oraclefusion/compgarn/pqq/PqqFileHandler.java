package com.adp.smartconnect.oraclefusion.compgarn.pqq;

import java.io.File;
import java.io.IOException;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.ClientConfiguration;
import com.adp.smartconnect.oraclefusion.compgarn.clientConfiguration.PqqFileDetails;
import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;
import com.adp.smartconnect.oraclefusion.compgarn.integration.client.FileMover;
import com.adp.smartconnect.oraclefusion.compgarn.listeners.ClientConfigHolder;
import com.adp.smartconnect.oraclefusion.compgarn.xmltypes.PQFile;
import com.adp.smartconnect.oraclefusion.compgarn.xmltypes.PreQualRecord;

public class PqqFileHandler implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(PqqFileHandler.class);

	private String clientName = null;

	private Configuration configuration;

	private ClientConfigHolder clientConfigurations;
	
	private ApplicationContext appCtx;
	
	private FileMover fileMover;

	private File getUTF8File(File input) throws IOException {

		// Change file encoding to UTF-8 if not already. We should also move the
		// file to Temp - Start
		logger.info("The absolute path is " + input.getAbsolutePath());
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
		return utf8File;
	}

	private PQFile getPQFile(File utf8File) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(PQFile.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		PQFile file = (PQFile) jaxbUnmarshaller.unmarshal(utf8File);
		return file;
	}

	private void callRunReport(PreQualRecord rec, PqqFileDetails fileDetails, String transID, String reportLocation)
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
		
		PqqLoadTask pqqLoadTask = (PqqLoadTask)this.appCtx.getBean("pqqLoadTask");

		pqqLoadTask.setUrl(fileDetails.getPqqUrl());
		pqqLoadTask.setLocale(fileDetails.getPqqLocale());
		pqqLoadTask.setChunkSizeDownload(-1);

		pqqLoadTask.callRunReport(fileDetails.getPqqReportPath(), data, fileDetails.getPqqUsername(),
				fileDetails.getPqqPassword(), fileDetails.getPqqFormat(), null, reportLocation);

	}

	private PQFile mergePQFileObjects(List<String> files) throws JAXBException {

		JAXBContext j = JAXBContext.newInstance(PQFile.class);
		Unmarshaller u = j.createUnmarshaller();
		List<PreQualRecord> records = new ArrayList<>();
		PQFile que = null;
		
		if(files.isEmpty()) {
			return null;
		}
		
		for (String s : files) {
			File f = new File(s);
			que = (PQFile) u.unmarshal(f);
			que.setTransmissionID(que.getTransId());
			que.setTransId(null);

			if (null != que.getPreQualRecordList() && null != que.getPreQualRecordList().getQualRecords()
					&& que.getPreQualRecordList().getQualRecords().size() != 0) {
				records.addAll(que.getPreQualRecordList().getQualRecords());
			}

		}

		if(null != que && null != que.getPreQualRecordList()) {
			que.getPreQualRecordList().setQualRecords(records);
		}
		
		return que;
	}

	private void createPQQResponseFile(ClientConfiguration config, PQFile que) throws JAXBException, IOException {

		// Create directory
		String pqqRespDir = configuration.getFileProcessingDir() + configuration.getPqqDir()
				+ configuration.getPqqOutboundDir();
		logger.info("Pqq response directory is " + pqqRespDir);

		String outputFile = getPQResponseFileName(config);

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

	}

	public File handleFile(File input) throws IOException {

		boolean isPartDone = false;
		boolean isError = false;
		boolean noAction = false;
		File inProcessingFile = null;
		File doneFile = null;
		File partFile = null;
		File errorFile = null;
		PqqFileDetails fileDetails = null;
		try {
			String fileName = input.getName();
			MDC.put("transactionID", fileName);
			logger.info("handleFile PqqFileHandler to handle the file " + input.getAbsolutePath());

			clientName = fileName.substring(0, 4).toLowerCase();
			logger.info("The client id in Pqq is " + clientName);
			ClientConfiguration config = clientConfigurations.getSingleClientData(clientName);

			// Check if the file is getting processed
			String dirName = FilenameUtils.getBaseName(fileName);
			fileDetails = config.getPqqFileDetails();
			inProcessingFile = new File(configuration.getFileProcessingDir() + configuration.getPqqDir()
					+ configuration.getPqqProcessedDir() + dirName + ".processing");
			doneFile = new File(configuration.getFileProcessingDir() + configuration.getPqqDir()
					+ configuration.getPqqProcessedDir() + dirName + ".done");
			partFile = new File(configuration.getFileProcessingDir() + configuration.getPqqDir()
					+ configuration.getPqqProcessedDir() + dirName + ".part.done");
			errorFile = new File(configuration.getFileProcessingDir() + configuration.getPqqDir()
					+ configuration.getPqqProcessedDir() + dirName + ".error");

			if (inProcessingFile.exists() || doneFile.exists() || partFile.exists() || errorFile.exists()) {
				noAction = true;
				return null;
			}
			// Create in processing file
			logger.info("inProcessingFile is " + inProcessingFile.getAbsolutePath());
			inProcessingFile.createNewFile();

			// Get the UTF 8 File and Place it in archive
			File utf8File = getUTF8File(input);
			PQFile file = getPQFile(utf8File);

			List<PreQualRecord> qualRecords = file.getPreQualRecordList().getQualRecords();
			String transID = file.getTransmissionID();
			int i = 1;
			List<String> files = new ArrayList<>();

			Map<String, Boolean> successRecs = new HashMap<>();
			Map<String, Boolean> failureRecs = new HashMap<>();

			for (PreQualRecord rec : qualRecords) {
				logger.debug("Pqq processing for the ith record " + i);
				String reportLocation = configuration.getFileProcessingDir() + configuration.getPqqDir()
						+ configuration.getPqqProcessedDir() + "/SubReport_"+ dirName + "_" + i + ".xml";		
				String key = null;
				try {
					key = rec.getSSN() + " " + rec.getEEFirstName();
					callRunReport(rec, fileDetails, transID, reportLocation);
					successRecs.put(key, true);
					files.add(reportLocation);
				} catch (Exception exc) {
					failureRecs.put(key, false);
					logger.error("Exeception while invoking the callRunReport", exc);
				}

				i = i + 1;
			}

			isPartDone = (successRecs.size() != 0 && failureRecs.size() != 0);
			
			if(isPartDone) {
				// Print the summary
				logger.error("Error happened for the following SSN and first name combinations ");
				Iterator<String> failRecsItr = failureRecs.keySet().iterator();
				while(failRecsItr.hasNext()) {
					String key = (String)failRecsItr.next();
					logger.error(key);
				}
				logger.error("Total "+ failureRecs.size() + " records failed the PQQ Processing.");
				logger.error("Total "+ successRecs.size() + " records succeeded the PQQ Processing.");
			}

			// Merge PQFile objects
			PQFile que = mergePQFileObjects(files);

			// Create the response file
			if(null != que) {
				createPQQResponseFile(config, que);
			}
			
			isError = (successRecs.size() == 0);

		} catch (Exception exc) {	
			logger.error("Exception in PqqFileHandler ", exc);
			errorFile.createNewFile();
			isError = true;	
		} finally {
			logger.debug("noAction " + noAction);
			logger.debug("isPartDone " + isPartDone);
			logger.debug("isError " + isError);
			
			if(!noAction) {
				
				if (!isError && isPartDone) {
					partFile.createNewFile();
				} else if(!isError) {
					doneFile.createNewFile();
				} else if (isError) {
					errorFile.createNewFile();
				}
				inProcessingFile.delete();

				logger.debug("Removing the file");
				fileMover.removeFile(input);
				
			}
			
		}
		MDC.clear();
		return null;
	}

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

	public static void main(String[] args) throws IOException, JAXBException {

		// String str = "XXX1MMDDYYYYHHMMSS.PREQ.PQQ";
		// System.out.println(str.substring(0, 3));
		//
		// System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump",
		// "true");
		// System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump",
		// "true");
		// System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump",
		// "true");
		// System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump",
		// "true");
		// System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpTreshold",
		// "65673576543765467537");
		//
		// String path =
		// FileHandler.class.getClassLoader().getResource("").getPath();
		// String fullPath = URLDecoder.decode(path, "UTF-8");
		// final File file = new File(path + "com/everge/clientConfigurations");
		// JAXBContext jaxbContext =
		// JAXBContext.newInstance(ClientConfiguration.class);
		// Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		// for (final File child : file.listFiles()) {
		// ClientConfiguration configuration =
		// (ClientConfiguration)jaxbUnMarshaller.unmarshal(child);
		// ApplicationContextReferencer.addClientConfiguration(configuration.getClientName(),
		// configuration);
		// }

		PqqFileHandler handler = new PqqFileHandler();

		File input = new File("/Users/abhisheksingh/ddrive/everge_ws/test/1.PQQ");
		JAXBContext jaxbContext = JAXBContext.newInstance(PQFile.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		PQFile file = (PQFile) jaxbUnmarshaller.unmarshal(input);

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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appCtx = applicationContext;		
	}

	public FileMover getFileMover() {
		return fileMover;
	}

	public void setFileMover(FileMover fileMover) {
		this.fileMover = fileMover;
	}

}