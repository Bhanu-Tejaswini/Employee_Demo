package com.arraigntech.controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arraigntech.model.ChannelDTO;
import com.arraigntech.model.ChannelErrorUIResponse;
import com.arraigntech.model.ChannelListUIResponse;
import com.arraigntech.model.ChannelStatus;
import com.arraigntech.model.ChannelUIResponse;
import com.arraigntech.model.CustomChannelDTO;
import com.arraigntech.model.UpdateAllTitleDTO;
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
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> addChannel(@RequestBody ChannelDTO channelDTO) {
		if(log.isDebugEnabled()) {
			log.debug("Adding channel {}.",channelDTO);
		}
		Boolean result = channelService.createChannel(channelDTO);
		BaseResponse<Boolean> response = new BaseResponse<>();
		return result
				? response.withSuccess(true)
						.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.CHANNEL_SUCCESS).build()
				: response.withSuccess(false)
						.withResponseMessage(MessageConstants.KEY_FAIL, MessageConstants.CHANNEL_FAIL)
						.build();

	}
	
	@ApiOperation(value = "Adding custom channel")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/custom", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> addCustomChannel(@RequestBody CustomChannelDTO customChannelDTO) {
		if(log.isDebugEnabled()) {
			log.debug("Adding custom channel {}.",customChannelDTO);
		}
		Boolean result = channelService.addInstagramChannel(customChannelDTO);
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
		if(log.isDebugEnabled()) {
			log.debug("Enabling channell {}.",channelStatus);
		}
		BaseResponse<String> response = new BaseResponse<>();
		String info=channelService.enableChannel(channelStatus);
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, info).build();
	}
	
	@ApiOperation(value = "Disabling channel")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/disable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> disableChannel(@RequestBody ChannelStatus channelStatus) {
		if(log.isDebugEnabled()) {
			log.debug("Disabling channel {}.",channelStatus);
		}
		BaseResponse<String> response = new BaseResponse<>();
		String info=channelService.disableChannel(channelStatus);
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, info).build();
	}
	
	@ApiOperation(value = "Get the list of channels of user")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/fetch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<List<ChannelUIResponse>> getChannelList() {
		if(log.isDebugEnabled()) {
			log.debug("Get the list of channels of user");
		}
		ChannelListUIResponse userChannels = channelService.getUserChannels();
		if(userChannels.isFlag())
			return new BaseResponse<List<ChannelUIResponse>>(userChannels.getChannelList()).withSuccess(true);
		else
			return new BaseResponse<List<ChannelUIResponse>>(userChannels.getChannelList()).withSuccess(false);
	}
	
	@ApiOperation(value = "Get the list of error channels")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/error", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<List<ChannelErrorUIResponse>> getErrorChannelList() {
		if(log.isDebugEnabled()) {
			log.debug("get the list of error channels");
		}
		return new BaseResponse<List<ChannelErrorUIResponse>>(channelService.getErrorChannels()).withSuccess(true);
	}
	
	
	@ApiOperation(value = "add channel title")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/title", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> addChannel(@RequestBody UpdateTitleDTO updateTitleDTO) {
		if(log.isDebugEnabled()) {
			log.debug("update channel title {}.",updateTitleDTO);
		}
		Boolean result = channelService.addUpdateTitle(updateTitleDTO);
		BaseResponse<Boolean> response = new BaseResponse<>();
		return result
				? response.withSuccess(true)
						.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.TITLE_SUCCESS).build()
				: response.withSuccess(false)
						.withResponseMessage(MessageConstants.KEY_FAIL, MessageConstants.TITLE_FAIL)
						.build();

	}
	
	@ApiOperation(value = "Add all channel title")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/title/all", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> updateChannelTitleAll(@RequestBody UpdateAllTitleDTO updateAllTitleDTO) {
		if(log.isDebugEnabled()) {
			log.debug("Add all channel title {}.",updateAllTitleDTO);
		}
		Boolean result = channelService.updateAllTitles(updateAllTitleDTO);
		BaseResponse<Boolean> response = new BaseResponse<>();
		return result
				? response.withSuccess(true)
						.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.TITLE_SUCCESS).build()
				: response.withSuccess(false)
						.withResponseMessage(MessageConstants.KEY_FAIL, MessageConstants.TITLE_FAIL)
						.build();

	}
	
	@ApiOperation(value = "Remove the channel")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> addChannel(@PathVariable String channelId) {
		if(log.isDebugEnabled()) {
			log.debug("Remove the channel {}",channelId);
		}
		Boolean result = channelService.removechannel(channelId);
		BaseResponse<Boolean> response = new BaseResponse<>();
		return result
				? response.withSuccess(true)
						.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.CHANNEL_REMOVED).build()
				: response.withSuccess(false)
						.withResponseMessage(MessageConstants.KEY_FAIL, MessageConstants.CHANNEL_REMOVED_FAIL)
						.build();

	}
	
	@ApiOperation(value = "Get the title of the channel")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/title/{channelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<UpdateTitleDTO> getChannelTitle(@PathVariable("channelId") String channelId) {
		if(log.isDebugEnabled()) {
			log.debug("Get the title of the channel {}",channelId);
		}
		return new BaseResponse<UpdateTitleDTO>(channelService.getUpdateTitle(channelId)).withSuccess(true);
	}
	

}
