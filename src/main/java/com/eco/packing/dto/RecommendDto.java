package com.eco.packing.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecommendDto {

	String size;
	List<ProductPackingDto> ppList = new ArrayList<>();
	
}
