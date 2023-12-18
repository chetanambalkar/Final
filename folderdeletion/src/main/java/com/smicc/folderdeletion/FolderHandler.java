package com.smicc.folderdeletion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.plaf.FileChooserUI;

public class FolderHandler {

	private int count = 0;
	private Configuration config;
	private static final Logger logger = Logger.getLogger(FolderHandler.class.getName());

	public FolderHandler(Configuration config) {
		this.config = config;
	}

	public void handleFolder(Path folderPath) throws IOException {
		System.out.println("Inside handleFolder method");
		File files = new File(folderPath.toString());
		iterateFilesInFolder(files.listFiles());
		System.out.println("End of handleFolder method");
	}

	private void iterateFilesInFolder(File[] listOfFiles) throws IOException {
		count = count + 1;
		System.out.println("Iteration Count:" + count);
		if (listOfFiles != null) {
			for (File file : listOfFiles) {
				if (file.isDirectory() == true) {
					System.out.println("Directory found in " + file.getAbsolutePath());
					iterateFilesInFolder(file.listFiles());

				} else {
					System.out.println("File found in " + file.getName());
					checkModifiedFiles(file);
				}
			}
		} else {
			System.out.println("No files are found in the folder");
		}
	}

	private void checkModifiedFiles(File file) throws IOException {

		Date lastModifiedDate = null;
		lastModifiedDate = new Date(Files.getLastModifiedTime(file.toPath()).toMillis());
		System.out.println("lastModified date:" + lastModifiedDate + " for file " + file.getName());
		if (isOlderThanThreshold(lastModifiedDate)) {
			moveFolderToTemp(file.toPath(), Paths.get(config.getTempPath()));
		}
	}

	private void moveFolderToTemp(Path source, Path destination) {
		try {
			System.out.println("Source path:" + source.toString());
			String resolvedPathString = String.valueOf(destination.resolve(destination.relativize(source).normalize()));
			String finalDestinationPath = resolvedPathString.replace("\\..\\", "\\");
			Path finalMovePath = Paths.get(finalDestinationPath);
			System.out.println("Final Destination Path:" + finalMovePath);
			System.out.println("Creating Directory in Temp folder");
			Path path1 = Files.createDirectories(finalMovePath.getParent());
			System.out.println("Directories created as per path:" + path1.toString());
			System.out.println("Moving source file to temp folder");
			Files.move(source, Paths.get(finalMovePath.toString()));
			System.out.println("File moved to temp folder: " + finalMovePath.toString());

		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error moving folder to temp folder", e);
		}
	}

	public void deleteFolderFromTemp(File folder) {
		System.out.println("Inside deleteFolderFormTemp method");
		try {
			Files.walk(folder.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
			System.out.println("Folder deleted from temp folder: " + folder.toPath().toString());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error deleting folder from temp folder", e);
		}
	}

	private boolean isOlderThanThreshold(Date lastModifiedDate) {
		System.out.println("Inside isOlderThanThreshold Method");
		long currentMillis = System.currentTimeMillis();
		long thresholdMillis = currentMillis - TimeUnit.DAYS.toMillis(config.getDeletionFrequencyDays());
		Date deletionThreshold = new Date(thresholdMillis);
		System.out.println("Deletion threshold date: " + deletionThreshold.toString());
		// return lastModifiedDate.before(deletionThreshold);
		return true;
	}

}