package com.eco.packing.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Order {

	@Id
	private String id;
	
	private String state;
	
	private String boxId;
	
}
