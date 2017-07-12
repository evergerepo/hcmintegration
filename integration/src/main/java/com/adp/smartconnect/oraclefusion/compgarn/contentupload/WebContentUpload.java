package com.adp.smartconnect.oraclefusion.compgarn.contentupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcClientProfile;
import oracle.stellent.ridc.IdcClientProvider;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.TransferFile;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class WebContentUpload {
	
	private static final Logger log = LoggerFactory.getLogger(WebContentUpload.class);
	
	private String clientUrl;
	
	private String userName;
	
	private String password;
	
	public WebContentUpload() {
		//
	}
	
	public WebContentUpload(String clientUrl, String userName, String password) {
		this.clientUrl = clientUrl;
		this.userName = userName;
		this.password = password;
	}

	public String uploadContent(String path) throws IdcClientException, FileNotFoundException, IllegalArgumentException {
		String contentId = "UCM";
		try {
			
			IdcClientManager m_clientManager = new IdcClientManager();
			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			String formattedDate = sdf.format(now);
			contentId = contentId + formattedDate;
			log.info("Content ID:" + contentId);
			IdcClient idcClient = m_clientManager.createClient(this.clientUrl);
			IdcClientProvider provider = null;
			
			// replace with relevant URL
			IdcContext userContext = new IdcContext(this.userName, this.password); 
			
			log.info("Username passed in IdcContext " + userName);
			log.info("Password passed in IdcContext " + password);
			
			// "D:\\Suresh\\Oracle_HCM_Fusion\\Automation\\HCC\\Worker.zip",
			// // Replace with fully
			// qualified path to source
			// file
			checkin(idcClient, userContext, path,
					"Document", // content type
					"Testing", // doc title
					userContext.getUser(), // author
					"FAFusionImportExport", // security group
					"hcm$/dataloader$/import$", // account
					contentId);// dDocName - this is the ContentId ;
			log.info("Upload is successful: " + contentId);
		} catch (Exception e) {
			log.error("uploadContent ",e);
			throw e;
		}

		return contentId;
	}

	/**
	 * * Method description * * @param idcClient * @param userContext * @param
	 * sourceFileFQP fully qualified path to source content * @param contentType
	 * content type * @param dDocTitle doc title * @param dDocAuthor author
	 * * @param dSecurityGroup security group * @param dDocAccount account
	 * * @param dDocName dDocName * * @throws IdcClientException
	 * @throws FileNotFoundException 
	 */
	public void checkin(IdcClient idcClient, IdcContext userContext, String sourceFileFQP, String contentType,
			String dDocTitle, String dDocAuthor, String dSecurityGroup, String dDocAccount, String dDocName)
			throws IdcClientException, FileNotFoundException {
		InputStream is = null;
		try {
			String fileName = sourceFileFQP.substring(sourceFileFQP.lastIndexOf('/') + 1);
			is = new FileInputStream(sourceFileFQP);
			long fileLength = new File(sourceFileFQP).length();
			TransferFile primaryFile = new TransferFile();
			primaryFile.setInputStream(is);
			primaryFile.setContentType(contentType);
			primaryFile.setFileName(fileName);
			primaryFile.setContentLength(fileLength); 

			DataBinder request = idcClient.createBinder();
			request.putLocal("IdcService", "CHECKIN_UNIVERSAL");
			request.addFile("primaryFile", primaryFile);
			request.putLocal("dDocTitle", dDocTitle);
			request.putLocal("dDocAuthor", dDocAuthor);
			request.putLocal("dDocType", contentType);
			request.putLocal("dSecurityGroup", dSecurityGroup);

			request.putLocal("dDocAccount", dDocAccount == null ? "" : dDocAccount);
			if (dDocName != null && dDocName.trim().length() > 0) {
				request.putLocal("dDocName", dDocName);
			} 
			
			// execute the request
			ServiceResponse response = idcClient.sendRequest(userContext, request); 

			DataBinder responseBinder = response.getResponseAsBinder();
			
		} catch (Exception e) {
			log.error("uploadContent ",e);
			throw e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignore) {
				}
			}
		}
	}
	
	public String getClientUrl() {
		return clientUrl;
	}

	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
