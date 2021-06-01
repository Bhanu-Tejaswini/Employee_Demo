package com.arraigntech.controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arraigntech.request.vo.ChannelStatusVO;
import com.arraigntech.request.vo.ChannelVO;
import com.arraigntech.request.vo.CustomChannelVO;
import com.arraigntech.request.vo.UpdateAllTitleVO;
import com.arraigntech.request.vo.response.BaseResponse;
import com.arraigntech.response.vo.ChannelErrorUIResponseVO;
import com.arraigntech.response.vo.ChannelListUIResponseVO;
import com.arraigntech.response.vo.ChannelUIResponseVO;
import com.arraigntech.response.vo.UpdateTitleVO;
import com.arraigntech.service.impl.ChannelServiceImpl;
import com.arraigntech.utility.MessageConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/channel")
public class ChannelsController {
	
	@Autowired
	private ChannelServiceImpl channelService;
	
	@ApiOperation(value = "Adding channel")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> addChannel(@RequestBody ChannelVO channelDTO) {
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
	public BaseResponse<Boolean> addCustomChannel(@RequestBody CustomChannelVO customChannelDTO) {
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
	public BaseResponse<String> enableChannel(@RequestBody ChannelStatusVO channelStatus) {
		BaseResponse<String> response = new BaseResponse<>();
		String info=channelService.enableChannel(channelStatus);
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, info).build();
	}
	
	@ApiOperation(value = "Disabling channel")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/disable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> disableChannel(@RequestBody ChannelStatusVO channelStatus) {
		BaseResponse<String> response = new BaseResponse<>();
		String info=channelService.disableChannel(channelStatus);
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, info).build();
	}
	
	@ApiOperation(value = "Get the list of channels of user")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/fetch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<List<ChannelUIResponseVO>> getChannelList() {
		ChannelListUIResponseVO userChannels = channelService.getUserChannels();
		if(userChannels.isFlag())
			return new BaseResponse<List<ChannelUIResponseVO>>(userChannels.getChannelList()).withSuccess(true);
		else
			return new BaseResponse<List<ChannelUIResponseVO>>(userChannels.getChannelList()).withSuccess(false);
	}
	
	@ApiOperation(value = "Get the list of error channels")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/error", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<List<ChannelErrorUIResponseVO>> getErrorChannelList() {
		return new BaseResponse<List<ChannelErrorUIResponseVO>>(channelService.getErrorChannels()).withSuccess(true);
	}
	
	
	@ApiOperation(value = "add channel title")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/title", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> addChannel(@RequestBody UpdateTitleVO updateTitleDTO) {
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
	public BaseResponse<Boolean> updateChannelTitleAll(@RequestBody UpdateAllTitleVO updateAllTitleDTO) {
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
	public BaseResponse<UpdateTitleVO> getChannelTitle(@PathVariable("channelId") String channelId) {
		return new BaseResponse<UpdateTitleVO>(channelService.getUpdateTitle(channelId)).withSuccess(true);
	}
	

}
