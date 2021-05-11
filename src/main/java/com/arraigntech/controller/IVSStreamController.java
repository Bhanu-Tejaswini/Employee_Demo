package com.arraigntech.controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arraigntech.entity.Channels;
import com.arraigntech.model.response.BaseResponse;
import com.arraigntech.service.impl.IVSStreamServiceImpl;
import com.arraigntech.streamsModel.IVSLiveStream;
import com.arraigntech.streamsModel.StreamSourceConnectionInformation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/stream")
public class IVSStreamController {
	
	public static final Logger log = LoggerFactory.getLogger(IVSStreamController.class);

	@Autowired
	private IVSStreamServiceImpl streamService;

	@ApiOperation(value = "Creating the live stream")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<StreamSourceConnectionInformation> createStream(@RequestBody IVSLiveStream liveStream) {
		log.debug("Creating the live stream");
		return new BaseResponse<StreamSourceConnectionInformation>(streamService.createStream(liveStream)).withSuccess(true);
	}

//	@PutMapping("/start/{id}")
//	public String startStream(@PathVariable("id") String id) {
//		String response = streamService.task(id);
//		return response;
//	}

	@PutMapping("/stop/{id}")
	public String stopStream(@PathVariable("id") String id) {
		return streamService.stopStream(id);
	}
	
	@GetMapping("/hello")
	public String hello(@CurrentSecurityContext(expression="authentication?.name")
	                    String username) {
	    return "Hello, " + username + "!";
	}
}
