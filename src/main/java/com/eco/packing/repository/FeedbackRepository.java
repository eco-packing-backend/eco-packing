package com.eco.packing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eco.packing.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>{

	
}
