package com.arraigntech.controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arraigntech.model.ChannelDTO;
import com.arraigntech.model.ChannelStatus;
import com.arraigntech.model.UpdateTitleDTO;
import com.arraigntech.model.response.BaseResponse;
import com.arraigntech.service.impl.ChannelServiceImpl;
import com.arraigntech.utility.MessageConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/channel")
public class ChannelsController {
	
	public static final Logger log = LoggerFactory.getLogger(ChannelsController.class);
	
	@Autowired
	private ChannelServiceImpl channelService;
	
	@ApiOperation(value = "Adding channel")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> addChannel(@RequestBody ChannelDTO channelDTO) {
		log.debug("Adding channel");
		Boolean result = channelService.createChannel(channelDTO);
		BaseResponse<Boolean> response = new BaseResponse<>();
		return result
				? response.withSuccess(true)
						.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.CHANNEL_SUCCESS).build()
				: response.withSuccess(false)
						.withResponseMessage(MessageConstants.KEY_FAIL, MessageConstants.CHANNEL_FAIL)
						.build();

	}
	
	@ApiOperation(value = "Enabling channel")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/enable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> enableChannel(@RequestBody ChannelStatus channelStatus) {
		log.debug("Enabling channel");
		BaseResponse<String> response = new BaseResponse<>();
		String info=channelService.enableChannel(channelStatus);
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, info).build();
	}
	
	@ApiOperation(value = "Disabling channel")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/disable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> disableChannel(@RequestBody ChannelStatus channelStatus) {
		log.debug("Disabling channel");
		BaseResponse<String> response = new BaseResponse<>();
		String info=channelService.disableChannel(channelStatus);
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, info).build();
	}
	
	@ApiOperation(value = "Get the list of channels of user")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<List<ChannelDTO>> getChannelList() {
		log.debug("Get the list of channels of user");
		return new BaseResponse<List<ChannelDTO>>(channelService.getUserChannels()).withSuccess(true);
	}
	
	
	@ApiOperation(value = "update channel title")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/title", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> addChannel(@RequestBody UpdateTitleDTO updateTitleDTO) {
		log.debug("update channel title");
		Boolean result = channelService.addUpdateTitle(updateTitleDTO);
		BaseResponse<Boolean> response = new BaseResponse<>();
		return result
				? response.withSuccess(true)
						.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.TITLE_SUCCESS).build()
				: response.withSuccess(false)
						.withResponseMessage(MessageConstants.KEY_FAIL, MessageConstants.TITLE_FAIL)
						.build();

	}
	
	@ApiOperation(value = "Get the title of the channel")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/title", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<UpdateTitleDTO> getChannelTitle(@RequestBody ChannelStatus channelStatus) {
		log.debug("Get the title of the channel");
		return new BaseResponse<UpdateTitleDTO>(channelService.getUpdateTitle(channelStatus)).withSuccess(true);
	}
	

}
