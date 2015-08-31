package com.test.zp.sort;

public class CommonUtils {

	public static void swap(int[] a, int i, int j){
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp ;
	}
	
	public static void toString(int[] a){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<10; i++){
			sb.append(a[i]).append("  ");
		}
		System.out.println(sb.toString());
	}
}
