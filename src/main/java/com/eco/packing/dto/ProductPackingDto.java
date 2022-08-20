package com.eco.packing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductPackingDto {
	
	private String id;
	private String name;
	private String material;
	private String length;
}
