package com.eco.packing.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.eco.packing.dto.BoxFeedbackDto;
import com.eco.packing.response.Response;
import com.eco.packing.response.ResponseCode;
import com.eco.packing.response.ResponseMessage;
import com.eco.packing.service.FeedbackService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FeedbackController {
	
	private final FeedbackService feedbackService;
	
	@PostMapping("eco/order/feedback")
	public Response getOrderFeedback(@RequestBody BoxFeedbackDto boxFeedbackDto) {
		
		feedbackService.saveFeedback(boxFeedbackDto);
		return new Response();
	}
}
