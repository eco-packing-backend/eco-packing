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

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.eco.packing.dto.BoxRecommendDto;
import com.eco.packing.dto.ProductPackingDto;
import com.eco.packing.dto.ProductRecommendDto;
import com.eco.packing.dto.RecommendDto;
import com.eco.packing.entity.Box;
import com.eco.packing.entity.Category;
import com.eco.packing.entity.Order;
import com.eco.packing.entity.OrderedProduct;
import com.eco.packing.entity.PackagingMaterial;
import com.eco.packing.entity.Product;
import com.eco.packing.entity.State;
import com.eco.packing.entity.StorageType;
import com.eco.packing.entity.Texture;
import com.eco.packing.repository.BoxRepository;
import com.eco.packing.repository.CategoryRepository;
import com.eco.packing.repository.OrderRepository;
import com.eco.packing.repository.OrderedProductRepository;
import com.eco.packing.repository.PackagingMaterialRepository;
import com.eco.packing.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

	private static class PackedBox {
		public Box box;
		public ArrayList<Product> products;
		public PackedBox(Box box, ArrayList<Product> products) {
			super();
			this.box = box;
			this.products = products;
		}
	}
	
	private final BoxRepository boxRepository;
	private final PackagingMaterialRepository packagingMaterialRepository;
	private final OrderedProductRepository orderedProductRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final OrderRepository orderRepository;
	
	public ArrayList<RecommendDto> recommend(String orderId) {
		
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
		List<Box> styrofoamBox = new ArrayList<>();
		List<Box> paperBox = new ArrayList<>();
		for(int i=0; i<boxList.size(); i++) {
			Box b = boxList.get(i);
			if(b.getTexture() == Texture.STYROFOAM)
				styrofoamBox.add(b);
			else
				paperBox.add(b);
		}
		
		// DB에 저장된 전체 포장재 리스트 -> 추후 배치로 변경
		List<PackagingMaterial> pmList = packagingMaterialRepository.findAll();

		List<OrderedProduct> opList = orderedProductRepository.getProductFromOrder(orderId);
		Map<String, Integer> productCount = new HashMap<>();
		
		
		for(int i=0; i<opList.size(); i++) {
			OrderedProduct op = opList.get(i);
			productCount.put(op.getId().getProductId(), op.getCount());
		}
		
		List<String> productIds = new ArrayList<>(productCount.keySet());
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
		
		// count가 1보다 많은 것들 추가로 arraylist에 저장
		int size = products.size();
		for(int i = 0; i<size; i++) {
			String productId = products.get(i).getId();
			if(productCount.get(productId) > 1) {
				int count = productCount.get(productId);
				for(int j=0; j< count-1; j++) {
					products.add(products.get(i));
				}
			}
		}
		
		// 반복문 - 냉동 + 냉장 / 상온
		ArrayList<Product> frozen = new ArrayList<>();
		
		for(int i=0; i<products.size(); i++) {
			if(products.get(i).getStorageType() == StorageType.FROZEN || products.get(i).getStorageType() == StorageType.COLD) {
				frozen.add(products.remove(i--));
			}
		}
		
		/*
		 * 추천 박스 알고리즘
		 */
//		Map<Box, ArrayList<Product>> recommends = new HashMap<>();
		
		// 박스 1개당 상품리스트 저장
		Queue<ArrayList<Product>> queue = new ArrayDeque<>();
		
		// order를 무게로 내림차순
		Collections.sort(products, new Comparator<Product>() {
			@Override
			public int compare(Product o1, Product o2) {
				return o1.getWeight() - o2.getWeight();
			}
		});

		/*
		 * 상품리스트 2개 - 냉동 + 냉장 / 상온
		 * 박스리스트 2개 - 종이 / 스티로폼
		 * 
		 * 냉동 - 스티로폼 / 냉장+상온 - 종이  
		 * 
		 */
		// 주문내역 상품 총합이 20kg 초과하는지 확인
		
		// 프론트에 전송하기 위해 박스당 DTO로 변환
		ArrayList<RecommendDto> result = new ArrayList<>();
		
		ArrayList<PackedBox> commonRecommends = seperateBox(products, paperBox, categorys);
		orderToDto(commonRecommends, result);
		if(frozen.size() > 0) {
			ArrayList<PackedBox> frozendRecommends = seperateBox(frozen, styrofoamBox, categorys);
			orderToDto(frozendRecommends, result);
		}
		
		return result;
	}
		
	public ArrayList<PackedBox> seperateBox(List<Product> products, List<Box> boxList, Map<String, Double> categorys) {
		
		/*
		 * 상품리스트 2개 - 냉동 / 냉장+상온
		 * 박스리스트 2개 - 종이 / 스티로폼
		 * 
		 * 냉동 - 스티로폼 / 냉장+상온 - 종이  
		 * 
		 */
		
		Queue<ArrayList<Product>> queue = new ArrayDeque<>();
		Queue<ArrayList<Product>> unCheckedProduct = new ArrayDeque<>();
		
		unCheckedProduct.offer((ArrayList<Product>) products);
		
		// 주문내역 상품 총합이 20kg 초과하는지 확인
		while(!unCheckedProduct.isEmpty()) {
			ArrayList<Product> pList = unCheckedProduct.poll();
			int totalWeight = 0;
			for(int i=0; i<pList.size(); i++) {
				totalWeight += pList.get(i).getWeight();
				if(totalWeight > 20000)
					break;
			}
		
			if(totalWeight > 20000) {
				ArrayList<Product> pList1 = new ArrayList<>();
				ArrayList<Product> pList2 = new ArrayList<>();
				
				Collections.sort(pList, new Comparator<Product>() {
					@Override
					public int compare(Product o1, Product o2) {
						return o1.getWeight() - o2.getWeight();
					}
				});
				
				int weight = 0;	// 하나의 박스에 20kg 가깝게 해서 pList1에 담고  나머지를 pList2에 담기
				
				for(int i=0; i<pList.size(); i++) {
					if(weight + pList.get(i).getWeight() < 20000) {
						weight += pList.get(i).getWeight();
						pList1.add(pList.get(i));
					}else {
						pList2.add(pList.get(i));
					}
					

				}
				
				queue.offer(pList1);
				unCheckedProduct.offer(pList2);
			} else {
				queue.offer(pList);
			}
		}
	
		// 박스당 20kg 내로 무게 측정
		
		// 박스 부피별로 내림차순
		Collections.sort(boxList, new Comparator<Box>() {
			@Override
			public int compare(Box o1, Box o2) {
				return o2.getVolume() - o1.getVolume();
			}
		});	
		
		ArrayList<Box> boxRecommends = new ArrayList<>();
		ArrayList<ArrayList<Product>> productRecommends = new ArrayList<>();
	
		ArrayList<PackedBox> recommends = new ArrayList<>();
		
		// 사이즈 안맞으면 주문내역 반으로 쪼개는 코드(무게 기준으로)
		while(!queue.isEmpty()) {
			ArrayList<Product> pList = queue.poll();
			Box recommendedBox = recommendBox(boxList, pList, categorys, queue);
			
			if(recommendedBox==null) {

			} else {
				recommends.add(new PackedBox(recommendedBox, pList));
			}
		}

		return recommends;
	}
	
	public Box recommendBox(List<Box> boxList, ArrayList<Product> products, Map<String, Double> categorys, Queue<ArrayList<Product>> q) {
		
		int maxWi = 0;
		int productTotalVolume = 0;
		
		ArrayList<ProductRecommendDto> prdList = new ArrayList<>();
		// 박스 추천 위해서 박스 -> 박스 DTO
		for (Product p : products) {
			int wi = p.getWidth();
			int he = p.getHeight();
			int hi = p.getHigh();
			maxWi = Math.max(maxWi, wi);
			productTotalVolume += p.getVolume();
		}

		Box recommendBox = null;
		
		for (int i = 0; i < boxList.size(); i++) {
			Box b = boxList.get(i);
			String id = b.getId();
			int wi = b.getWidth();
			int he = b.getHeight();
			int hi = b.getHigh();
			// 1. 상품 중 최대 가로길이 < 박스 길이 2. 상품 종 부피 < 박스 총 부피
//			if (wi > maxWi && b.getVolume() > productTotalVolume) {
			if (wi > maxWi) {
				ArrayList<Product> copyList = new ArrayList<>(products);
				if (!canPutAllProducts(copyList, new BoxRecommendDto(wi, he, hi), id, q)) {
					return recommendBox;
				}
				recommendBox = b;
			}
		}
		return recommendBox;
	}

	public void orderToDto(ArrayList<PackedBox> recommends, ArrayList<RecommendDto> result) {		
		for(PackedBox pb : recommends) {
			
			ArrayList<ProductPackingDto> pprList = new ArrayList<>();
			for(Product p : pb.products) {
				ProductPackingDto prd = new ProductPackingDto(p.getId(), p.getName(), p.getPackagingMaterial().getName(), Double.toString(p.getPackagingMaterialQuantity()));
				pprList.add(prd);
			}
			
			result.add(new RecommendDto(pb.box.getName(), pb.box.getTexture().name(), pprList));
		}
	}
	
	public boolean canPutAllProducts(ArrayList<Product> prdList, BoxRecommendDto brd, String boxid, Queue<ArrayList<Product>> q) {

		// 부피순으로 정렬
		Collections.sort(prdList, new Comparator<Product>() {
			@Override
			public int compare(Product o1, Product o2) {
				return o2.getVolume() - o1.getVolume();
			}
		});

		brd.put(prdList);

		if (prdList.size() == 1) {
			int realVolume = 0;

			for (Product p : brd.arranged) {
				realVolume += p.getMinVolume();
			}

			if ((brd.maxWidth * brd.maxHeight * brd.maxHigh) * 0.8 - realVolume > prdList.get(0).getMinVolume()) {
				brd.arranged.add(prdList.get(0));
				prdList.remove(0);
			}
		}

		if (prdList.size() > 0) {
			if(boxid.equals("5") || boxid.equals("7")) {
				q.add(brd.arranged);
				q.add(prdList);
			}
			
			return false;
		}

		return true;
	}

	@Transactional
	public void completeOrder(String orderId) {
		Order order = orderRepository.findById(orderId).get();
		order.setState(State.COMPLETE);
	}
}
