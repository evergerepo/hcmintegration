package com.adp.smartconnect.oraclefusion.compgarn.file.processing;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PqqFileFilter<F> extends FileListFilter<F>{
	
	protected static final Logger log = LoggerFactory.getLogger(PqqFileFilter.class);

	private static String ACCEPTABLE_XTENSIONS = ".pqq";

	@Override
	protected boolean accept(F file) {
		File f = (File) file;
		if(super.accept(file)==false){
			return false;
		}
		
		if(shouldFilebeAccepted(f)) {
			boolean doesFileExist = isFileExisting(configuration.getFileProcessingDir() + configuration.getPqqDir() + configuration.getPqqArchiveDir(), f.getName());
			if(doesFileExist){
				log.warn(">>>>>>>>>Input file in archive dir, stop processing file."+f.getAbsolutePath());
				return false;
			}
			return true;
		}else {
			log.error(">>>>>>FILE PATTERN NOT MATCHED, MOVE FILR TO ERROR FOLDER. {}", f.getAbsolutePath());
			moveToErrorDir(configuration.getFileProcessingDir()+configuration.getPqqDir()+configuration.getPqqErrorDir(), f);
			return false;
		}
		
	}
	
	protected String[] getFileExtensions() {
		String[] extensions = ACCEPTABLE_XTENSIONS.split(",");
		return extensions;
	}

}
