package com.eco.packing.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Box {

	@Id
	private String id;
	
	private String name;
	
	private int width;
	
	private int height;
	
	private int high;

	public int getVolume() {
		return width * height * high;
	}
}
