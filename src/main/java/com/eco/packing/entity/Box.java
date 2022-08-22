package com.eco.packing.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

	@Enumerated(EnumType.STRING)
	private Texture texture;
	
	public int getVolume() {
		return width * height * high;
	}
	
}
