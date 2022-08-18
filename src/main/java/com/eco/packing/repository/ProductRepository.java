package com.eco.packing.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eco.packing.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

	Optional<Product> findById(String id);
	
	ArrayList<Product> findByIdIn(List<String> idList);
}
