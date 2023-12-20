package com.noelwon.service;

import org.springframework.stereotype.Service;

@Service
public class UtilBox {
	
	
	public int[] cal(int[] intarr, int[] add) {
		int a = 0;
		int[] result = new int[6];
		for (int i = 0; i < result.length; i++) {
			a += add[i];
		}
		double value = (double) a / result.length;
		for (int i = 0; i < add.length; i++) {
			if (add[i] <= value) {
				add[i] = -1;
			} else {
				add[i] = 1;
			}
		}
		for (int i = 0; i < add.length; i++) {
			if ((intarr[i] >= 10 && add[i] == 1) || (intarr[i] <= 0 && add[i] == -1)) {
				result[i] = intarr[i];
			} else {
				result[i] = intarr[i] + add[i];
			}
		}
		return result;
	}
	
}
