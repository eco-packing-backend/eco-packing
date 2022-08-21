package com.eco.packing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

		for (MaterialFeedbackDto mf : mfList) {
			Feedback fb = new Feedback(0L, FeedbackType.PACKAGING_MATERIAL, null, productMap.get(mf.getId()),
					Integer.parseInt(mf.getMaterialFeedback()), 0);
			feedbackRepository.save(fb);
			if (!boxFeedbackDto.getBoxFeedback().equals("0")) {
				fb = new Feedback(0L, FeedbackType.BOX, null, productMap.get(mf.getId()),
						Integer.parseInt(mf.getMaterialFeedback()), 0);
				feedbackRepository.save(fb);
			}
		}
		
	}

}
