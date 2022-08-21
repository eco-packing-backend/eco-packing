package com.eco.packing.dto;

import java.util.ArrayList;

import com.eco.packing.controller.MaterialFeedbackDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoxFeedbackDto {

	public String boxFeedback;
	public ArrayList<MaterialFeedbackDto> materials;
	
}
