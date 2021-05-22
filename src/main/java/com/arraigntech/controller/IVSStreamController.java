package com.arraigntech.controller;

import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arraigntech.model.response.BaseResponse;
import com.arraigntech.service.IVSStreamService;
import com.arraigntech.streamsModel.StreamUIRequest;
import com.arraigntech.streamsModel.StreamUIResponse;
import com.arraigntech.utility.MessageConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/stream")
public class IVSStreamController {
	
	public static final Logger log = LoggerFactory.getLogger(IVSStreamController.class);

	@Autowired
	private IVSStreamService streamService;

	@ApiOperation(value = "Creating the live stream")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<StreamUIResponse> createStream(@RequestBody StreamUIRequest streamRequest) {
		log.debug("Creating the live stream");
		return new BaseResponse<StreamUIResponse>(streamService.createStream(streamRequest)).withSuccess(true);
	}

	@ApiOperation(value = "Stop the live stream")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/stop/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> stopStream(@PathVariable("id") String id) {
		log.debug("Stop the live stream");
		String result = streamService.stopStream(id);
		BaseResponse<String> response=new BaseResponse<>();
		return response.withSuccess(true).withResponseMessage(MessageConstants.KEY_SUCCESS, result).build();
	}
	
	@ApiOperation(value = "delete the live stream")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/{streamId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> deleteStream(@PathVariable("streamId") String id) {
		log.debug("delete the live stream");
		boolean result = streamService.deleteStream(id);
		BaseResponse<String> response=new BaseResponse<>();
		return result
				? response.withSuccess(true)
						.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.STREAM_REMOVED).build()
				: response.withSuccess(false)
						.withResponseMessage(MessageConstants.KEY_FAIL, MessageConstants.STREAM_REMOVED_FAIL)
						.build();

	}
	
	@ApiOperation(value = "Fetch Stream status")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/status/{streamId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> fetchStreamStatus(@PathVariable("streamId") String id) {
		log.debug("Fetch Stream status");
		String result = streamService.fetchStreamState(id);
		BaseResponse<String> response=new BaseResponse<>();
		return response.withSuccess(true).withResponseMessage(MessageConstants.KEY_SUCCESS, result).build();
	}

}
