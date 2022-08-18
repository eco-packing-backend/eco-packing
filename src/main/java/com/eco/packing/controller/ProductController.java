package com.eco.packing.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eco.packing.entity.Product;
import com.eco.packing.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	
	@GetMapping("/product/{productId}")
	public Product getProduct(@PathVariable("productId")String productId) {
		
		Product p = productService.getProduct(productId);
		System.out.println(p.getId() + " : " + p.getName());
		return p;
	}
	
	@GetMapping("/order/{orderId}")
	public List<Product> getOrder(@PathVariable("orderId")String orderId) {
		
		return productService.getProductByOrderId(orderId);
	}
}
