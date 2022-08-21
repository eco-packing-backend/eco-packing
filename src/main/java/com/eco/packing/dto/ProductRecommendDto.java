package com.eco.packing.dto;

import java.util.Arrays;
import java.util.Collections;

public class ProductRecommendDto {

	int width; // 가로
	int height; // 세로
	int high; // 높이
	int[][] position; // 여러 방향으로 돌렸을 때 나올 수 있는 가로, 세로, 높이
	double error; // 오차

	public ProductRecommendDto(double width, double height, double high, double error) {
		// 가로 > 세로 > 높이 순으로 정렬되도록 함
		Integer[] temp = new Integer[3];
		// 길이에 소수점이 있을 경우 값을 올림처리
		temp[0] = Integer.valueOf((int) Math.ceil(width));
		temp[1] = Integer.valueOf((int) Math.ceil(height));
		temp[2] = Integer.valueOf((int) Math.ceil(high));
		Arrays.sort(temp, Collections.reverseOrder());

		this.width = temp[0];
		this.height = temp[1];
		this.high = temp[2];

		// 밑변의 넓이가 가장 큰 순서대로 저장
		position = new int[][] { { this.width, this.height, this.high }, { this.height, this.width, this.high },
				{ this.width, this.high, this.height }, { this.high, this.width, this.height },
				{ this.height, this.high, this.width }, { this.high, this.height, this.width } };

		this.error = error;
	}

	/**
	 * getMaxVolume : 오차를 적용하지 않은 부피를 계산해주는 함수
	 * 
	 * @return 오차를 적용하지 않은 부피
	 */
	public int getMaxVolume() {
		return width * height * high;
	}

	/**
	 * getMinVolume : 오차를 적용한 부피를 계산해주는 함수
	 * 
	 * @return 오차를 적용한 부피
	 */
	public double getMinVolume() {
		return (width * height * high) * (100 - error) / 100.0;
	}

}
