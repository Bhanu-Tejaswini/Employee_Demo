package com.employee.controller;

import java.net.HttpURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.employee.request.vo.EmployeeVO;
import com.employee.request.vo.LoginDetailsVO;
import com.employee.response.vo.BaseResponse;
import com.employee.response.vo.LoginResponseVO;
import com.employee.service.impl.EmployeeServiceImpl;


@RestController
@RequestMapping("/auth")
public class EmployeeController {
	@Autowired
	private EmployeeServiceImpl userService;



	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> saveAndUpdateEmployee(@RequestBody EmployeeVO user) {	
		return new BaseResponse<Boolean>(userService.register(user)).withSuccess(true);
	}


	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> login(@RequestBody LoginDetailsVO login) {
		BaseResponse<String> response = new BaseResponse<>();
		LoginResponseVO responseMessage = userService.generateToken(login);
		return responseMessage.isFlag()
				? response.withSuccess(true)
						.withResponseMessage("Token", responseMessage.getResult()).build()
				: response.withSuccess(false)
						.withResponseMessage("Message", responseMessage.getResult())
						.build();

	}




}
