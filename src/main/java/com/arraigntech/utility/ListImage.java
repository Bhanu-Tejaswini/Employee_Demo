package com.arraigntech.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.arraigntech.model.ImageModel;

@Component
public class ListImage {
	
	// SDCard Path
	//choose your path for me i choose sdcard
	final String MEDIA_PATH = "./src/main/resources/imagesList";
			//Environment.getExternalStorageDirectory().getAbsolutePath() + "/Your Folder Name/Path";
	private ArrayList<ImageModel> imageList = new ArrayList<>();

	// Constructor
	public ListImage() {

	}

	public ArrayList<ImageModel> getImagesList() throws IOException {

	    File home = new File(MEDIA_PATH);
	    if (home.listFiles(new FileExtensionFilter()) != null) {
	        if (home.listFiles(new FileExtensionFilter()).length > 0) {
	            for (File file : home.listFiles(new FileExtensionFilter())) {
	                ImageModel imageModel = new ImageModel();
	                imageModel .setaName(file.getName().substring(0, (file.getName().length() - 4)));
	                imageModel .setaPath(file.getPath());
	                byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
	                imageModel.setImageBytes(bytes);
	                imageList.add(imageModel);
	            }
	        }
	    }
	    return imageList;
	}

}
