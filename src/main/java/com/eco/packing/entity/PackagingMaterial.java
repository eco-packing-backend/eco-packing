package com.eco.packing.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PackagingMaterial {
	
	@Id
	private String id;
	
	private String name;
	
	private int width;
	
	private int height;
	
	private int high;
	
	@OneToMany(mappedBy="packagingMaterial")
	List<Product> productList = new ArrayList<>();
	
}
