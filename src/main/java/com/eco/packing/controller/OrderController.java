package com.eco.packing.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eco.packing.dto.ProductPackingDto;
import com.eco.packing.dto.RecommendDto;
import com.eco.packing.response.Response;
import com.eco.packing.response.ResponseCode;
import com.eco.packing.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {

	
	@GetMapping("eco/order/{orderId}")
	public Response<ArrayList<RecommendDto>> getRecommendBox(@PathVariable("orderId") String orderId) {
		
		ArrayList<ProductPackingDto> pp1 = new ArrayList<>();
		ProductPackingDto ppd1 = new ProductPackingDto("1", "최지성", "얼음팩", "10개");
		ProductPackingDto ppd2 = new ProductPackingDto("2", "이하림", "종이", "3장");
		
		pp1.add(ppd1);
		pp1.add(ppd2);
		
		ArrayList<ProductPackingDto> pp2 = new ArrayList<>();
		ProductPackingDto ppd3 = new ProductPackingDto("3", "박종수", "종이", "1장");
		
		pp2.add(ppd1);
		pp2.add(ppd3);
		
		RecommendDto rd1 = new RecommendDto("1ȣ", pp1);
		RecommendDto rd2 = new RecommendDto("2ȣ", pp2);
		
		ArrayList<RecommendDto> result = new ArrayList<>();
		result.add(rd1);
		result.add(rd2);
		
		if(orderId.equals("1")) {
			return new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, result);
		}
		
		return new Response(ResponseCode.ILLEGAL_ARGUMENT, ResponseMessage.INVALID_PARAMETER);
	}
}
