package com.adp.smartconnect.oraclefusion.compgarn.file.processing;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.integration.file.filters.AbstractFileListFilter;

import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;

public class EvergeFileListFilter<F> extends AbstractFileListFilter<F> {

	private static final Logger log = LoggerFactory.getLogger(EvergeFileListFilter.class);
	
	private Configuration configuration;
	
	@Override
	protected boolean accept(F file) {

		File f = (File) file;

		MDC.put("fileName", f.getName());
		log.info("Input File : ["+f.getAbsolutePath()+"]");

		if(f.getAbsolutePath().contains(".working")) {
			log.warn("File is Working file. Wait for some time to complete process......");
			return false;
		}
		
		/*
		 * Check for PQQ file. If file name exists in "archive" folder then remove file from "inbound" folder 
		 *  and stop processing the file
		 */
		if(f.getAbsolutePath().contains(".pqq")) {
			String pqqArchiveLocation = configuration.getFileProcessingDir() + configuration.getPqqDir() + configuration.getPqqArchiveDir();
			String archiveFileName = pqqArchiveLocation + f.getName();
			log.info("Checking if the file has already been processed in archive folder: "+f.getName());
			
			File archiveFile = new File(archiveFileName);
			final File dir = new File(pqqArchiveLocation);
			
			File[] files = dir.listFiles();
			if(null == files) {
				return true;
			}
			
			if(archiveFile.exists()) {
				log.error("PQQ file has already been processed, removing file from inbound folder. File Name:"+f.getName());
				removeFile(f);
				return false;
			}else{
				log.info("PQQ File received " + f.getName());
			}
			return true;
		}else if ((f.getAbsolutePath().contains(".txt") || f.getAbsolutePath().contains(".grn")) && !f.getAbsolutePath().contains(".writing")) {
			log.info("File " + f.getAbsolutePath() + " is accepted for evaluation");
			String notifArchiveLocation = configuration.getFileProcessingDir() + configuration.getLienArchiveDir();
			String fullName = notifArchiveLocation + f.getName();
			log.info("Check if the file has already been processed " + fullName);
			File fl = new File(fullName);
			if(fl.exists()) {
				log.error("The file has already been processed."+f.getName());
				removeFile(f);
				return false;
			}else {
				log.info("File  is accepted for processing:"+f.getName());
			}
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private void moveFile(File input, String newDir) {
		File f1 = new File(newDir + input.getName());

		if (input.renameTo(f1)) {
			log.info("File moved successful :" + input.getAbsolutePath());
		} else {
			log.info("File failed to move :" + input.getAbsolutePath());
		}
		
	}
	
	private void removeFile(File input) {
		input.delete();
	}
	
	public static void main(String[] args) {
		File f = new File("/Users/abhisheksingh/ddrive/everge_ws/f/Sample Lien File-112916-NEW.txt");
		EvergeFileListFilter<File> obj = new EvergeFileListFilter<>();
		obj.accept(f);
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
