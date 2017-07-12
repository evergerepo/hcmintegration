package com.adp.smartconnect.oraclefusion.compgarn.file.processing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.file.filters.AbstractFileListFilter;

import com.adp.smartconnect.oraclefusion.compgarn.config.Configuration;

public class EvergeFileListFilter<F> extends AbstractFileListFilter<F> {

	protected static final Logger log = LoggerFactory.getLogger(EvergeFileListFilter.class);
	
	protected Configuration configuration;
	
	@Override
	protected boolean accept(F file) {
		
		File f = (File) file;
		
		if(f.getAbsolutePath().contains(".working") || f.getAbsolutePath().contains(".writing")) {
			// wait for some time
			return false;
		}
		
		return true;
		
	}
	
	protected void removeFile(File input) {
		input.delete();
	}
	
	protected void moveToErrorDir(String errorDir, File file) {
		
		File newFile = new File(errorDir + file.getName());
		try {
			Files.copy(file.toPath(), newFile.toPath());
			removeFile(file);
		} catch (IOException e) {
			log.error("Error while moving the file to error directory",e);
		}

	}
	
	protected boolean isFileExisting(String archiveLocation, String fileName) {	
		
		String fullName = archiveLocation + fileName;
		log.info("Check if the file has already been processed " + fullName);

		File fl = new File(fullName);
		if(fl.exists()) {
			
			log.error("The file has already been processed.");
			removeFile(fl);
			return true;
			
		}
		log.info("File " + fl.getAbsolutePath() + " is accepted for processing");
		return false;
		
	}
	
	protected String[] getFileExtensions() {
		return null;
	}
	
	protected boolean shouldFilebeAccepted(File file) {
		
		int extensionIndex =FilenameUtils.indexOfExtension(file.getName());
		String extension = file.getName().substring(extensionIndex);
		String[] eligibleExtensions = getFileExtensions();
		boolean shouldFilebeAccepted = false;
		for(String s : eligibleExtensions) {
			if(extension.equals(s)) {
				shouldFilebeAccepted = true;
				break;
			}
		}
		return shouldFilebeAccepted;
		
	}
	
	public static void main(String[] args) {
		File f = new File("/Users/abhisheksingh/ddrive/everge_ws/f/Sample Lien File-112916-NEW.txt");
//		EvergeFileListFilter<File> obj = new EvergeFileListFilter<>();
//		obj.accept(f);
		
		EvergeFileListFilter<File> filter = new LienFileFilter<>();
		boolean x = filter.shouldFilebeAccepted(f);
		System.out.println("x is " + x);
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
