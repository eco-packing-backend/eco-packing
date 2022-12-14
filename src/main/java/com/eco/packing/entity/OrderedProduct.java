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
public class OrderedProduct {
	
	@EmbeddedId
	private OrderedProductId id;
	
	@MapsId("id.orderId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;
	
	@MapsId("id.productId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="product_id")
	private Product product;
	
	private int count;
	
	public void changeOrder(Order order) {
		if(this.order != null) {
			this.order.getOrderedProductList().remove(this);
		}
		this.order = order;
		if(!order.getOrderedProductList().contains(this)) {
			order.getOrderedProductList().add(this);
		}
	}
}
