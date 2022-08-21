package com.eco.packing.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.eco.packing.dto.BoxFeedbackDto;
import com.eco.packing.response.Response;
import com.eco.packing.response.ResponseCode;
import com.eco.packing.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FeedbackController {
	
	@PostMapping("eco/feedback")
	public Response getFeedback(@RequestBody BoxFeedbackDto boxFeedbackDto) {
		
		System.out.println(boxFeedbackDto.getBoxFeedback());
		
		return new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, boxFeedbackDto);
	}
}
