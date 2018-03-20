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

import com.adp.smartconnect.oraclefusion.compgarn.listeners.APPConfigHolder;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.TransferFile;
import oracle.stellent.ridc.protocol.ServiceResponse;
import oracle.stellent.ridc.protocol.http.IdcHttpClientConfig;

public class WebContentUpload {
	
	private static final Logger log = LoggerFactory.getLogger(WebContentUpload.class);
	
	
/*
 * Upload Lien file to Content Server
 */
	@SuppressWarnings("rawtypes")
	public String uploadContent(String clientUrl, String userName, String password, String path) 
			throws IdcClientException, FileNotFoundException, IllegalArgumentException, Exception {
		String contentId = "UCM";
		try {
			
			IdcClientManager m_clientManager = new IdcClientManager();
			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			String formattedDate = sdf.format(now);
			contentId = contentId + formattedDate;
			
			log.info("ContentUpload ContentId:" + contentId);
			
			IdcClient idcClient = m_clientManager.createClient(clientUrl);
			
			// replace with relevant URL
			IdcContext userContext = new IdcContext(userName, password); 
			
			log.debug("ContentUpload: Username:" + userName+", Password:"+password+", URL:"+clientUrl);
			
			checkin(idcClient, userContext, path,
					"Document", // content type
					"Testing", // doc title
					userContext.getUser(), // author
					"FAFusionImportExport", // security group
					"hcm$/dataloader$/import$", // account
					contentId);// dDocName - this is the ContentId ;
			
			log.info("Lien Notification file Upload is successful. ContentId:" + contentId);
		} catch (Exception e) {
			log.error("Lien Notification file Upload ERROR. Message:"+e.getMessage(),e);
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
	@SuppressWarnings("rawtypes")
	public void checkin(IdcClient idcClient, IdcContext userContext, String sourceFileFQP, String contentType,
			String dDocTitle, String dDocAuthor, String dSecurityGroup, String dDocAccount, String dDocName)
			throws Exception {
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
			

			//Check Proxy Config
			if(APPConfigHolder.isProxyEnabled()){
				log.info("Proxy configured. :"+APPConfigHolder.getProxyHost()+","+APPConfigHolder.getProxyPort());
				 IdcHttpClientConfig httpConfig = (IdcHttpClientConfig)idcClient.getConfig();
				 httpConfig.setUseSystemProxy(true);
			 }
			
			
			// execute the request
			ServiceResponse response = idcClient.sendRequest(userContext, request); 
			log.debug("Lien Notification file upload response: " + response.getResponseAsString());
			
		} catch (Exception e) {
			log.error("Lien Notification file Upload Error. Message: "+e.getMessage(),e);
			throw new IdcClientException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignore) {
				}
			}
		}
	}
	

}
