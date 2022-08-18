package com.eco.packing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

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
