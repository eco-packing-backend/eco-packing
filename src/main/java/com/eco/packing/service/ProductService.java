package com.eco.packing.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.eco.packing.dto.ProductGet;
import com.eco.packing.entity.Order;
import com.eco.packing.entity.Product;
import com.eco.packing.repository.OrderRepository;
import com.eco.packing.repository.OrderedProductRepository;
import com.eco.packing.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final OrderedProductRepository orderedProductRepository;
	
	public Product getProduct(String id) {
		
		return productRepository.findById(id).get();
	}
	
	public List<Product> getProductByOrderId(String orderId) {
		
//		Order o = orderRepository.findById(id).get();
		ArrayList<String> idList = orderedProductRepository.getProductFromOrderedProduct(orderId);
		
//		ArrayList<Product> pList = productRepository.findByIdIn(idList);
		ArrayList<ProductGet> gList = orderedProductRepository.getProductFromOrderedProduct2(orderId);
		ArrayList<Product> pList = new ArrayList<>();
		
		for(int i=0; i<gList.size(); i++) {
			ProductGet g = gList.get(i);
			pList.add(new Product(g.getId(), g.getWidth(), g.getHeight(), g.getHigh(), g.getWeight(), g.getName(), g.getPackagingMaterialId(), g.getPackagingMaterialQuantity(), g.getFragile()));
		}
		return pList;
	}
}
