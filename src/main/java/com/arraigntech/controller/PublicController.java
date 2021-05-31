package com.arraigntech.controller;

import java.net.HttpURLConnection;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arraigntech.model.AccountSettingVO;
import com.arraigntech.model.response.BaseResponse;
import com.arraigntech.service.AccountSettingService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class PublicController {
	
	public static final Logger log = LoggerFactory.getLogger(PublicController.class);
	
	@Autowired
	protected AccountSettingService accountSettingService;
	
	@GetMapping("/prevent")
	public Principal getGogle(Principal principal) {
		return principal;
	}
	
	
	@ApiOperation(value = "timezonelist and country list")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/timezonelist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<AccountSettingVO> getTimeZonesList() {
		return new BaseResponse<AccountSettingVO>(accountSettingService.getTimeZonesList()).withSuccess(true);
	}
}
