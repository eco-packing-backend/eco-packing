package com.eco.packing.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class RecommendedBoxId implements Serializable {

	@Column(name="order_id")
	private String orderId;
	
	@Column(name="box_id")
	private String boxId;
	
	
}
