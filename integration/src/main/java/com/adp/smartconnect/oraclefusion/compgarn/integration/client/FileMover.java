package com.adp.smartconnect.oraclefusion.compgarn.integration.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMover {
	
	private static final Logger log = LoggerFactory.getLogger(FileMover.class);
	
	public static void removeFile(File f) {
		if(f.exists()){
			f.delete();
		}
	}
	
	/*
	 * Copy file to Output Folder
	 */
	public static boolean handleFile(File input, String outputDir) throws IOException {
		log.info(" Trying to move the file ["+ input.getAbsolutePath()+"] to ["+outputDir+"]");
		boolean isFileMoved = false;
		try{
		
			
			String name = input.getName();
			StringTokenizer tokenizer = new StringTokenizer(name, ".");
			String extension = null;
			while(tokenizer.hasMoreTokens()) {
				extension = tokenizer.nextToken();
			}
			
			File f1 = null;
			
			// Create directory
			new File(outputDir).mkdirs();
			
			f1 = new File(outputDir + FilenameUtils.removeExtension(input.getName()) + "." + extension);
	
			if(input.exists()){
				Files.copy(input.toPath(), f1.toPath());
				isFileMoved = true;
			}else{
				log.error("Filenot found for moving file  ["+ input.getAbsolutePath()+"] to ["+outputDir+"]");
			}
			
			
			
		}catch(Exception e){
			log.error("Error moving the file  ["+ input.getAbsolutePath()+"] to ["+outputDir+"]");
		}
		return isFileMoved;
	}
	
	
	/*
	 * Merge list of files to one File
	 */
	public static void mergeFiles(List<File> files, File mergedFile) {

	        FileWriter fstream = null;
	        BufferedWriter out = null;
			
	        try
	        {
	            fstream = new FileWriter(mergedFile, true);
	            out = new BufferedWriter(fstream);
	        }
	        catch(IOException e1)
	        {
	            log.error("Exception while merging files ", e1);
	        }
			
	        log.debug("Merging both the files ");
			
	        for(File f : files)
	        {
	            FileInputStream fis;
	            try
	            {
	                fis = new FileInputStream(f);
	                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
	 
	                String aLine;
	                while((aLine = in.readLine()) != null)
	                {
	                    out.write(aLine);
	                    out.newLine();
	                }
	 
	                in.close();
	            }
	            catch(IOException e)
	            {
	               log.error("Exception while reading the file", e);
	            }
	        }
	        System.out.print("\nMerged Successfully..!!");
	 
	        try
	        {
	            out.close();
	        }
	        catch(IOException e)
	        {
	            log.error("Exception while closing the BufferedWriter ", e);
	        }
	    
	}
	
	public static void main(String[] args) throws IOException {
		String archiveLocation = "/Users/abhisheksingh/ddrive/everge_ws/pqqArchive/";
		File input = new File("/Users/abhisheksingh/ddrive/everge_ws/pqqResp/TEST.PQQ");
		String s = archiveLocation + FilenameUtils.removeExtension(input.getName()) + ".PQQ";
		System.out.println(s);
		File f1 = new File(s);
		//Files.copy(input.toPath(), f1.toPath());
		FileMover.removeFile(input);
	}

}
