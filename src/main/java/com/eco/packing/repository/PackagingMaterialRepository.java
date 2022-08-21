package com.eco.packing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.packing.entity.PackagingMaterial;

public interface PackagingMaterialRepository extends JpaRepository<PackagingMaterial, String>{

}
