package com.smicc.folderdeletion;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoFolderDeletion {

	public static void main(String[] args) {

		System.out.println("Starting delete utility");
		Configuration config = ConfigurationLoader.loadConfiguration();

		FolderProcessor folderProcessor = new FolderProcessor(config);

		try {
			folderProcessor.processFolders();
			
		} catch (IOException e) {
			Logger.getLogger(AutoFolderDeletion.class.getName()).log(Level.SEVERE,
					"Error during file and folder processing", e);
		}

		// Sleep for the specified frequency
		/*
		 * try {
		 * Thread.sleep(TimeUnit.DAYS.toMillis(config.getDeletionFrequencyDays())); }
		 * catch (InterruptedException e) { Thread.currentThread().interrupt(); //
		 * Restore the interrupted status
		 * Logger.getLogger(AutoFolderDeletion.class.getName()).log(Level.WARNING,
		 * "Thread interrupted", e); }
		 */
	}
}