package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.IVSLiveStream;
import com.example.demo.model.IVSLiveStreamResponse;
import com.example.demo.service.IVSStreamService;

@RestController
@RequestMapping("/vstreem")
public class IVSStreamController {

	@Autowired
	private IVSStreamService streamService;

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
