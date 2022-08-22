package com.eco.packing.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Order {

	@Id
	private String id;
	
	@Enumerated(EnumType.STRING)
	private State state;
	
	@OneToMany(mappedBy="id.orderId")
	private List<OrderedProduct> orderedProductList = new ArrayList<>();
	
	@OneToMany(mappedBy="id.orderId")
	private List<RecommendedBox> recommendedBoxList = new ArrayList<>();
}
