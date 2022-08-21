package com.eco.packing.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.eco.packing.dto.BoxRecommendDto;
import com.eco.packing.dto.ProductPackingDto;
import com.eco.packing.dto.ProductRecommendDto;
import com.eco.packing.dto.RecommendDto;
import com.eco.packing.entity.Box;
import com.eco.packing.entity.Category;
import com.eco.packing.entity.PackagingMaterial;
import com.eco.packing.entity.Product;
import com.eco.packing.repository.BoxRepository;
import com.eco.packing.repository.CategoryRepository;
import com.eco.packing.repository.OrderedProductRepository;
import com.eco.packing.repository.PackagingMaterialRepository;
import com.eco.packing.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

	private final BoxRepository boxRepository;
	private final PackagingMaterialRepository packagingMaterialRepository;
	private final OrderedProductRepository orderedProductRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public ArrayList<RecommendDto> printOrder(String orderId) {
		
		/*
		 * 필요한 데이터들
		 * 1. 박스(Box) 종류
		 * 2. 포장재(PackagingMaterial) 종류
		 * 3. 주문 내역 속에 있는 상품들 
		 * 4. 3번의 상품들에 해당하는 카테고리 종류들
		 * 
		 */
		// DB에 저장된 전체 박스 리스트 -> 추후 배치로 변경
		List<Box> boxList = boxRepository.findAll();
		
		// DB에 저장된 전체 포장재 리스트 -> 추후 배치로 변경
		List<PackagingMaterial> pmList = packagingMaterialRepository.findAll();
		
		// 주문번호 (orderId)로 주문 내역에 포함된 상품들(products) 가져오기 
		List<String> productIds = orderedProductRepository.getProductFromOrderedProduct(orderId);
		ArrayList<Product> products = productRepository.findByIdIn(productIds);
		
		// order에 담긴 상품들에 해당하는 카테고리의 오차율 집합
		Map<String, Double> categorys = new HashMap<>();
		for(Product p : products) {
			categorys.put(p.getCategory().getType(), null);
		}
		
		List<Category> categoryList = categoryRepository.findByTypeIn(categorys.keySet());
		
		for(Category c : categoryList) {
			categorys.put(c.getType(), c.getErrorRate());
		}
		
		/*
		 * 추천 박스 알고리즘
		 */
		
		Map<Box, ArrayList<Product>> recommends = new HashMap<>();
		
		ArrayDeque<ArrayList<Product>> queue = new ArrayDeque<>();
		queue.offer(products);
		
		Collections.sort(boxList, new Comparator<Box>() {
			@Override
			public int compare(Box o1, Box o2) {
				return o2.getVolume() - o1.getVolume();
			}
		});	
		
		while(!queue.isEmpty()) {
			ArrayList<Product> pList = queue.poll();
			Box recommendedBox = recommendBox(boxList, pList, categorys);
			
			if(recommendedBox==null) {
				
				// 나눠서 큐에 넣는다.
				
			} else {
				recommends.put(recommendedBox, pList);
			}
		}

		ArrayList<RecommendDto> result = new ArrayList<>();
		
		for(Box b : recommends.keySet()) {
			ArrayList<Product> pList = recommends.get(b);
			ArrayList<ProductPackingDto> pprList = new ArrayList<>();
			
			for(Product p : pList) {
				ProductPackingDto prd = new ProductPackingDto(p.getId(), p.getName(), p.getPackagingMaterial().getName(), Integer.toString(p.getPackagingMaterialQuantity()));
				pprList.add(prd);
			}
			
			result.add(new RecommendDto(b.getName(), pprList));
		}
		
		return result;
	}

	public Box recommendBox(List<Box> boxList, ArrayList<Product> products, Map<String, Double> categorys) {

		int maxWi = 0;
		int productTotalVolume = 0;
		
		ArrayList<ProductRecommendDto> prdList = new ArrayList<>();
		// 박스 추천 위해서 박스 -> 박스 DTO
		for (Product p : products) {
			int wi = p.getWidth();
			int he = p.getHeight();
			int hi = p.getHigh();
			maxWi = Math.max(maxWi, wi);
			ProductRecommendDto prd = new ProductRecommendDto(wi, he, hi, categorys.get(p.getCategory().getType()));
			productTotalVolume += p.getVolume();
			prdList.add(prd);
		}

		Box recommendBox = null;
		for (int i = 0; i < boxList.size(); i++) {
			Box b = boxList.get(i);

			int wi = b.getWidth();
			int he = b.getHeight();
			int hi = b.getHigh();
			// 1. 상품 중 최대 가로길이 < 박스 길이 2. 상품 종 부피 < 박스 총 부피
			if (wi > maxWi && b.getVolume() > productTotalVolume) {
				if (!canPutAllProducts(prdList, new BoxRecommendDto(wi, he, hi))) {
					return recommendBox;
				}
				recommendBox = b;
			}

		}
//		// 박스 중에 넣을 수 있는게 없음
//		if(recommendBox == null) {
//			
//		}

		return null;
	}

	public boolean canPutAllProducts(ArrayList<ProductRecommendDto> prdList, BoxRecommendDto brd) {

		// 부피순으로 정렬
		Collections.sort(prdList, new Comparator<ProductRecommendDto>() {
			@Override
			public int compare(ProductRecommendDto o1, ProductRecommendDto o2) {
				return o2.getMaxVolume() - o1.getMaxVolume();
			}
		});

		brd.put(prdList);

		if (prdList.size() == 1) {
			int realVolume = 0;

			for (ProductRecommendDto prd : brd.arranged) {
				realVolume += prd.getMinVolume();
			}

			if ((brd.maxWidth * brd.maxHeight * brd.maxHigh) * 0.8 - realVolume > prdList.get(0).getMinVolume()) {
				brd.arranged.add(prdList.get(0));
				prdList.remove(0);
			}

		}

		if (prdList.size() > 0)
			return false;

		return true;
	}
}
