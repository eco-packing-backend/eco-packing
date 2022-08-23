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
import javax.persistence.Transient;

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
	
	// 상품 정보 가져올 때 packagingMaterial도 같이 가져오기
	@ManyToOne
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
	
	private double error;
	
	public int getVolume() {
		return width * height * high;
	}
	
	public void updatePackagingMaterialQuantity(int feedback) {
		this.packagingMaterialQuantity += feedback;
	}
	
	/**
	 * getMaxVolume : 오차를 적용하지 않은 부피를 계산해주는 함수
	 * 
	 * @return 오차를 적용하지 않은 부피
	 */
	public int getMaxVolume() {
		return width * height * high;
	}

	/**
	 * getMinVolume : 오차를 적용한 부피를 계산해주는 함수
	 * 
	 * @return 오차를 적용한 부피
	 */
	public double getMinVolume() {
		return (width * height * high) * (100 - error) / 100.0;
	}
}
