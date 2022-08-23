package com.eco.packing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.eco.packing.common.FeedbackPoint;
import com.eco.packing.dto.BoxFeedbackDto;
import com.eco.packing.dto.MaterialFeedbackDto;
import com.eco.packing.entity.Feedback;
import com.eco.packing.entity.FeedbackType;
import com.eco.packing.entity.Product;
import com.eco.packing.repository.FeedbackRepository;
import com.eco.packing.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FeedbackService {

	private final FeedbackRepository feedbackRepository;
	private final ProductRepository productRepository;

	// 매 1시간마다 최신순 스케줄 리스트 최신화
	@Scheduled(cron="0 0 0/1 1/1 * ?")
	public void updateFeedback() {
		
		// 종이 포장재
		double paperAverage = FeedbackPoint.paperTotal / (double)FeedbackPoint.paperCount++;
		// 냉장 포장재
		double frozenAverage = FeedbackPoint.coldTotal / (double)FeedbackPoint.coldCount++;
		
	}
	
	@Transactional
	public void saveFeedback(BoxFeedbackDto boxFeedbackDto) {

		/*
		 * 박스 1개 피드백 상품 n개 피드백
		 */

		ArrayList<MaterialFeedbackDto> mfList = boxFeedbackDto.getMaterials();
		Map<String, Product> productMap = new HashMap<>();

		for (MaterialFeedbackDto mf : mfList) {
			productMap.put(mf.getId(), null);
		}

		ArrayList<Product> productList = productRepository.findByIdIn(new ArrayList<>(productMap.keySet()));
		for (Product p : productList) {
			productMap.put(p.getId(), p);
			
		}

		// 박스에 이상 -> 상품 오차율 수장 . 포장재 이상 -> 포장재 양 수정
		for (MaterialFeedbackDto mf : mfList) {
			
			Product p = productMap.get(mf.getId());
			// 박스에 이상 o
			
			// 포장재 수정 정보 저장
			int point = Integer.parseInt(mf.getMaterialFeedback());
			
			switch(p.getPackagingMaterial().getName()) {
			case "종이포장":
				double result = p.getPackagingMaterialQuantity() * (1-0.02*point);
				p.updatePackagingMaterialQuantity(result);
				break;
				
			}
				
			Feedback fb = new Feedback(0L, FeedbackType.PACKAGING_MATERIAL, null, p,
					Integer.parseInt(mf.getMaterialFeedback()), 0);
			feedbackRepository.save(fb);
			
			
			if (!boxFeedbackDto.getBoxFeedback().equals("0")) { // 박스에는 이상 x
				fb = new Feedback(0L, FeedbackType.BOX, null, productMap.get(mf.getId()),
						Integer.parseInt(mf.getMaterialFeedback()), 0);
				feedbackRepository.save(fb);
			}
		}
		
	}

}
