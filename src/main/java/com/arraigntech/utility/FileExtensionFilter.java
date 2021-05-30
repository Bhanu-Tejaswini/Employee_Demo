package com.arraigntech.utility;

import java.io.File;
import java.io.FilenameFilter;

public class FileExtensionFilter implements FilenameFilter {
	@Override
	public boolean accept(File dir, String name) {
        return (name.endsWith(".jpg") || name.endsWith(".JPG") || name.endsWith(".png") || name.endsWith(".mp4") || name.endsWith(".MP4")); // add more conditions here
	}

}
