package com.smicc.folderdeletion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderProcessor {

    private Configuration config;

    public FolderProcessor(Configuration config) {
        this.config = config;
    }

    public void processFolders() throws IOException {
    	System.out.println("Inside processFolders method");
    	 FolderHandler folderHandler = new FolderHandler(config);
    	 
    	 File tmpFile = new File(config.getTempPath());
    	 if(!tmpFile.exists()) {
    		 Files.createDirectories(Paths.get(config.getTempPath()));
    	 }
    	 
    	 folderHandler.deleteFolderFromTemp(new File(config.getTempPath())); 
    	 
        for (String folderName : config.getFolderNames()) {
        	System.out.println("Folder Name:"+folderName);
        	System.out.println("Root path:"+config.getRootPath());
            Path folderPath = Paths.get(config.getRootPath(), folderName);
          
            folderHandler.handleFolder(folderPath);
        }
    }

}