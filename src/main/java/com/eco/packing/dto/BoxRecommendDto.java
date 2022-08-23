package com.eco.packing.dto;

import java.util.ArrayList;
import java.util.List;

import com.eco.packing.common.Point;
import com.eco.packing.entity.Product;
import com.eco.packing.entity.StorageType;

public class BoxRecommendDto {

	public int width; // 가로
	public int height; // 세로
	public int high; // 높이
	public int[][][] space; // 내부 공간
	public ArrayList<Product> arranged; // 상자에 배치된 상품들

	public int maxWidth;
	public int maxHeight;
	public int maxHigh;

	public BoxRecommendDto(int width, int height, int high) {
		this.width = width;
		this.height = height;
		this.high = high;
		this.space = new int[high][height][width];
		arranged = new ArrayList<>();
	}

	/**
	 * getBoxVolume : 상자의 부피를 계산해주는 함수
	 * 
	 * @return 상자의 부피
	 */
	public int getBoxVolume() {
		return width * height * high;
	}

	/**
	 * findSpot : 상자 내에 상품 p가 들어갈 수 있는 좌표를 찾아주는 함수
	 * 
	 * @param p 현재 상자에 넣으려는 상품
	 * @return 현재 상자에 상품 p를 넣을 수 있는 공간이 있으면 공간의 좌표, 없으면 null
	 */

	public Point findSpot(ProductRecommendDto p, boolean isFrozen) {
		// 상자 내부 전체 탐색
		for (int i = 0; i < this.high; i++) {
			for (int j = 0; j < this.height; j++) {
				for (int k = 0; k < this.width; k++) {
					// 현재 좌표에 아무 상품도 존재하지 않으면
					if (this.space[i][j][k] == 0) {
						// 조금씩 돌려가면서
						for (int l = 0; l < 6; l++) {
							int width = p.position[l][0];
							int height = p.position[l][1];
							int high = isFrozen ? p.position[l][2] + 2 : p.position[l][2];

							// 돌렸을 때 상자에 넣을 수 있다면
							if (k + width <= this.width && j + height <= this.height && i + high <= this.high) {
								p.width = width;
								p.height = height;
								p.high = high;

								// i, j, k를 시작 좌표로 하여 넣었을 때, 중간에 겹치는 상품이 있는지 검사
								// 겹치는 상품이 있다면 그 공간에 넣을 수 없음
								boolean canPut = true;
								check: for (int m = i; m < i + high; m++) {
									for (int n = j; n < j + height; n++) {
										for (int o = k; o < k + width; o++) {
											// 겹치는 상품이 있다면
											if (this.space[m][n][o] != 0) {
												canPut = false;
												break check;
											}
										}
									}
								}

								// 겹치는 상품이 없다면
								if (canPut)
									return new Point(k, j, i);
							}
						}
					}
				}
			}
		}

		// 넣을 수 있는 공간이 없으면 null
		return null;
	}

	/**
	 * put : 상자에 상품들을 배치하는 함수
	 * 
	 * @param productList 상자에 담길 상품들 리스트
	 * @return 상품을 배치하고 배치하지 못한 상품들이 담긴 List
	 */
	public void put(List<Product> productList) {

		int index = 1;

		for (int pNum = 0; pNum < productList.size(); pNum++) {
			// 현재 상품
			Product now = productList.get(pNum);
			ProductRecommendDto p = new ProductRecommendDto(now.getWidth(), now.getHeight(), now.getHigh(),
					now.getError());

			Point point = null;
			if (now.getStorageType() == StorageType.WARM) {
				point = findSpot(p, false);
			} else {
				point = findSpot(p, true);
			}

			// 배치가 가능하다면
			if (point != null) {
				int x = point.x;
				int y = point.y;
				int h = point.h;

				// 상자에 상품을 배치
				for (int i = h; i < h + p.high; i++) {
					for (int j = y; j < y + p.height; j++) {
						for (int k = x; k < x + p.width; k++) {
							this.space[i][j][k] = index;
						}
					}
				}

				// 상자에 들어간 최대 가로, 세로, 높이 길이 측정
				if (maxWidth < x + p.width)
					maxWidth = x + p.width;
				if (maxHeight < y + p.height)
					maxHeight = y + p.height;
				if (maxHigh < h + p.high)
					maxHigh = h + p.high;

				arranged.add(now); // 넣은 상품을 리스트에 담고
				productList.remove(pNum--); // 상품 리스트에서는 제거
			}
			index++;
		}
	}
}
