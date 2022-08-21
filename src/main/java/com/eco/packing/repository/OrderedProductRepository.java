package com.eco.packing.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eco.packing.dto.ProductDto;
import com.eco.packing.dto.ProductGet;
import com.eco.packing.entity.OrderedProduct;
import com.eco.packing.entity.Product;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, String>{

	@Query(value="SELECT p.id \n" + 
			"FROM ordered_product op join product p ON (op.product_id = p.id)\n" + 
			"WHERE op.order_id = :orderId", nativeQuery = true)
	ArrayList<String> getProductFromOrderedProduct(@Param(value="orderId")String orderId);
	
//	@Query(value="SELECT p.id as id, p.width as width, p.height as height, p.high as high, p.weight as weight, p.name as name, p.packaging_material_id as packagingMaterialId,"
//			+ " p.packaging_material_quantity as packagingMaterialQuantity, p.fragile as fragile \n" + 
//			"FROM ordered_product op join product p ON (op.product_id = p.id)\n" + 
//			"WHERE op.order_id = :orderId", nativeQuery = true)
//	ArrayList<ProductGet> getProductFromOrderedProduct2(@Param(value="orderId")String orderId);
//	
}
