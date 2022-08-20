package com.eco.packing.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="packaging_material_id")
	private PackagingMaterial packagingMaterial;
	
	private int packagingMaterialQuantity;
	
	private int fragile;
	
	@Enumerated(EnumType.STRING)
	private StorageType storageType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="category_type")
	private Category category;
	
	@OneToMany(mappedBy="product")
	private List<Feedback> feedbackList = new ArrayList<>();
}
