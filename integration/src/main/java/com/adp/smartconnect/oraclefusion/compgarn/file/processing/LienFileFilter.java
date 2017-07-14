package com.adp.smartconnect.oraclefusion.compgarn.file.processing;

import java.io.File;

public class LienFileFilter<F> extends FileListFilter<F>{
	
	private static String ACCEPTABLE_XTENSIONS = ".txt,.grn";
	
	@Override
	protected boolean accept(F file) {
				
		File f = (File) file;
		if(super.accept(file)==false){
			return false;
		}
		
		if (shouldFilebeAccepted(f)) {
			boolean doesFileExist = isFileExisting(configuration.getFileProcessingDir() + configuration.getLienArchiveDir(), f.getName());
			if(doesFileExist){
				log.warn("Input file in archive dir, stop processing file."+f.getAbsolutePath());
				return false;
			}
			return true;
		} else {
			log.error("File patten is not matched, move file to error folder. :" + f.getAbsolutePath());
			moveToErrorDir(configuration.getLienErrorDir(), f);
			moveToErrorDir(configuration.getFileProcessingDir()+configuration.getLienDr()+configuration.getLienErrorDir(), f);
			
			return false;
		}
	}
	
	protected String[] getFileExtensions() {
		String[] extensions = ACCEPTABLE_XTENSIONS.split(",");
		return extensions;
	}

}
