package com.eco.packing.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	@Id
	private String id;
	
	private int width;
	
	private int height;
	
	private int high;
	
	private int weight;
	
	private String name;
	
	private String packagingMaterialId;
	
	private int packagingMaterialQuantity;
	
	private int fragile;
}
