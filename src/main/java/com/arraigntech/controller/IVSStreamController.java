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
import com.arraigntech.streamsModel.IVSLiveStream;
import com.arraigntech.streamsModel.StreamSourceConnectionInformation;
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
	public BaseResponse<StreamSourceConnectionInformation> createStream(@RequestBody IVSLiveStream liveStream) {
		log.debug("Creating the live stream");
		return new BaseResponse<StreamSourceConnectionInformation>(streamService.createStream(liveStream)).withSuccess(true);
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

}
