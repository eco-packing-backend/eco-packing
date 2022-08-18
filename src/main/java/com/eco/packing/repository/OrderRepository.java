package com.eco.packing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.packing.entity.Order;
import com.eco.packing.entity.Product;

public interface OrderRepository extends JpaRepository<Order, String>{

	Optional<Order> findById(String id);
	
}
