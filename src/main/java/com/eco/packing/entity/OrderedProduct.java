package com.eco.packing.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderedProduct {
	
	@EmbeddedId
	private OrderedProductId id;
	
	private int count;
}
