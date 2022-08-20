package com.eco.packing.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecommendedBox {

	@EmbeddedId
	private RecommendedBoxId id;
	
	@MapsId("id.orderId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;
	
	@MapsId("id.boxId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="box_id")
	private Box box;
	
	private int count;
	
	public void changeOrder(Order order) {
		if(this.order != null) {
			this.order.getRecommendedBoxList().remove(this);
		}
		this.order = order;
		if(!order.getRecommendedBoxList().contains(this)) {
			order.getRecommendedBoxList().add(this);
		}
	}
}
