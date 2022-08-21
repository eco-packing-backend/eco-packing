package com.eco.packing.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.packing.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{

	List<Category> findByTypeIn(Set<String> categoryIds);
}
