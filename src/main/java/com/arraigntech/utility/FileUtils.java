package com.arraigntech.utility;

import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.arraigntech.Exception.AppException;

public class FileUtils {
	
	public static final Logger log = LoggerFactory.getLogger(FileUtils.class);
	
	public static File getTempFilePath() {
		return new java.io.File(System.getProperty("java.io.tmpdir"));
	}

	public static File convertMultipartFileToFile(MultipartFile multipartFile) {
		try {
			if (multipartFile != null) {
				File convFile = new File(
						FileUtils.getTempFilePath() + File.separator + multipartFile.getOriginalFilename());
				try (FileOutputStream fos = new FileOutputStream(convFile)) {
					fos.write(multipartFile.getBytes());
				} catch (Exception e) {
					log.error("Error occured while closing output stream " + e);
					throw new AppException("Something went wrong, Please try again later.");
				}
				return convFile;
			}
			return null;
		} catch (Exception e) {
			log.error("Error occured while reading vendor banner image file " + e);
			throw new AppException("Something went wrong, Please try again later.");
		}
	}

	public static Boolean validateFile(MultipartFile file, String fileExtentions) {
		String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
		log.debug("File extention: " + extension);
		if (fileExtentions.contains(extension))
			return true;
		else
			return false;
	}

}
