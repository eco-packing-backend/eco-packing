package com.eco.packing.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category {

	@Id
	private String type;
	
	private double errorRate;
	
	@OneToOne(mappedBy="category")
	private Product product;
}
