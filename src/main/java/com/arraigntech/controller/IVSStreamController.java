package com.arraigntech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arraigntech.model.IVSLiveStream;
import com.arraigntech.model.IVSLiveStreamResponse;
import com.arraigntech.service.impl.IVSStreamServiceImpl;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/vstreem")
public class IVSStreamController {
	
	public static final Logger log = LoggerFactory.getLogger(IVSStreamController.class);

	@Autowired
	private IVSStreamServiceImpl streamService;

	@PostMapping("/create")
	public IVSLiveStreamResponse createStream(@RequestBody IVSLiveStream liveStream) {
		IVSLiveStreamResponse liveStreamResponse = streamService.createStream(liveStream);
		return liveStreamResponse;
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
