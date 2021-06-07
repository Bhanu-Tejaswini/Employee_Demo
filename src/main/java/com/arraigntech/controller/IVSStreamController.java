package com.arraigntech.controller;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arraigntech.exceptions.AppException;
import com.arraigntech.response.vo.BaseResponse;
import com.arraigntech.response.vo.S3UIResponse;
import com.arraigntech.service.DocumentS3Service;
import com.arraigntech.service.IVSStreamService;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.UtilEnum;
import com.arraigntech.wowza.request.vo.StreamUIRequestVO;
import com.arraigntech.wowza.response.vo.FetchStreamUIResponseVO;
import com.arraigntech.wowza.response.vo.StreamUIResponseVO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/stream")
public class IVSStreamController {

	@Autowired
	private IVSStreamService streamService;

	@Value("${mutlipartfile.size}")
	private int multiPartFileSize;

	@Autowired
	private DocumentS3Service s3Service;

	@ApiOperation(value = "Creating the live stream")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<StreamUIResponseVO> createStream(@RequestBody StreamUIRequestVO streamRequest) {
		return new BaseResponse<StreamUIResponseVO>(streamService.createStream(streamRequest)).withSuccess(true);
	}

	@ApiOperation(value = "Stop the live stream")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/stop/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> stopStream(@PathVariable("id") String id) {
		String result = streamService.stopStream(id);
		BaseResponse<String> response = new BaseResponse<>();
		return response.withSuccess(true).withResponseMessage(MessageConstants.KEY_SUCCESS, result).build();
	}

	@ApiOperation(value = "delete the live stream")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/{streamId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> deleteStream(@PathVariable("streamId") String id) {
		boolean result = streamService.deleteStream(id);
		BaseResponse<String> response = new BaseResponse<>();
		return result
				? response.withSuccess(true)
						.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.STREAM_REMOVED).build()
				: response.withSuccess(false)
						.withResponseMessage(MessageConstants.KEY_FAIL, MessageConstants.STREAM_REMOVED_FAIL).build();

	}

	@ApiOperation(value = "Fetch Stream status")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/status/{streamId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<FetchStreamUIResponseVO> fetchStreamStatus(@PathVariable("streamId") String id) {
		return new BaseResponse<FetchStreamUIResponseVO>(streamService.fetchStreamState(id)).withSuccess(true).build();
	}

	@ApiOperation(value = "upload image file")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(method = RequestMethod.POST, value = "/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BaseResponse<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file, String type) {
		if (file.getSize() > (multiPartFileSize * 1024 * 1024)) {
			throw new AppException(MessageConstants.FILE_SIZE_ERROR);
		}
		if (!UtilEnum.BRANDLOGO.name().equalsIgnoreCase(type)) {
			throw new AppException(MessageConstants.INVALID_REQUEST);
		}
		String documentPath = s3Service.uploadFile(file);
		return new BaseResponse<Map<String, String>>(s3Service.saveAWSDocumentDetails(type, documentPath)).withSuccess(true);
	}

	@ApiOperation(value = "delete image file")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/delete/file/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> deleteFile(@PathVariable String id) {
		BaseResponse<String> response = new BaseResponse<>();
		Boolean documentPath = s3Service.deleteFileFromS3Bucket(id);
		return documentPath
				? response.withSuccess(true).withResponseMessage(MessageConstants.KEY_SUCCESS,
						"file deleted successfully")
				: response.withSuccess(true).withResponseMessage(MessageConstants.KEY_SUCCESS,
						"Exception in deleting the file to S3");
	}

	@ApiOperation(value = "get image file")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/get/file", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<List<S3UIResponse>> getFile() {
		return new BaseResponse<List<S3UIResponse>>(s3Service.getDocumentImageURL()).withSuccess(true);
	}
	
	@ApiOperation(value = "update image file")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/update/file/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> updateSelectedImage(@PathVariable String id) {
		return new BaseResponse<Boolean>(s3Service.updateSelectedImage(id)).withSuccess(true);
	}
	
	@ApiOperation(value = "update default image file")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/update/file", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> setDefaultImage() {
		BaseResponse<String> response = new BaseResponse<>();
		Boolean result = s3Service.useDefaultImage();
		return result
				? response.withSuccess(true).withResponseMessage(MessageConstants.KEY_SUCCESS,
						"Logo set to Default image")
				: response.withSuccess(true).withResponseMessage(MessageConstants.KEY_SUCCESS,
						"Exception in setting the image to default");
	}

}
