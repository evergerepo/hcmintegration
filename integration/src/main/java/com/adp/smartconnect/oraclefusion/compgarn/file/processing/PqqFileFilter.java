package com.adp.smartconnect.oraclefusion.compgarn.file.processing;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PqqFileFilter<F> extends EvergeFileListFilter<F>{
	
	protected static final Logger log = LoggerFactory.getLogger(EvergeFileListFilter.class);

	private static String ACCEPTABLE_XTENSIONS = ".PQQ";

	@Override
	protected boolean accept(F file) {
		File f = (File) file;
		if(super.accept(file)){
			return true;
		}
		
		if(shouldFilebeAccepted(f)) {
			
			boolean doesFileExist = isFileExisting(configuration.getFileProcessingDir() + configuration.getPqqDir() + configuration.getPqqArchiveDir(), f.getName());
			if(doesFileExist){
				//No need to process
				return false;
			}
			return true;
		}else {
			log.error("PQQ file is blocked from further processing " + f.getAbsolutePath());
			moveToErrorDir(configuration.getLienErrorDir(), f);
			return false;
		}
		
	}
	
	protected String[] getFileExtensions() {
		String[] extensions = ACCEPTABLE_XTENSIONS.split(",");
		return extensions;
	}

}
