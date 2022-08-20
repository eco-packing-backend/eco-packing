package com.eco.packing.entity;

import java.util.List;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category {

	@Id
	private String type;
	
	private double errorRate;
	
	@OneToMany(mappedBy="category")
	private List<Product> product = new ArrayList<>();
}
