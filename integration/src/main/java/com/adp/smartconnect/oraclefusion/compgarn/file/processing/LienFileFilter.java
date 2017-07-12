package com.adp.smartconnect.oraclefusion.compgarn.file.processing;

import java.io.File;

public class LienFileFilter<F> extends EvergeFileListFilter<F>{
	
	private static String ACCEPTABLE_XTENSIONS = ".txt,.grn";
	
	@Override
	protected boolean accept(F file) {
				
		File f = (File) file;
		if(super.accept(file)){
			return true;
		}
		
		if (shouldFilebeAccepted(f)) {
			boolean doesFileExist = isFileExisting(configuration.getFileProcessingDir() + configuration.getLienArchiveDir(), f.getName());
			if(doesFileExist){
				//No need to process
				return false;
			}
			return true;
		} else {
			moveToErrorDir(configuration.getLienErrorDir(), f);
			return false;
		}
	}
	
	protected String[] getFileExtensions() {
		String[] extensions = ACCEPTABLE_XTENSIONS.split(",");
		return extensions;
	}

}
