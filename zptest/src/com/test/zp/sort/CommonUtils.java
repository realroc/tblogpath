package com.test.zp.sort;

public class CommonUtils {
	
	/**
	 * initial an random array
	 * @param length
	 * @return
	 */
	public static int[] generateArray(int length){
		int[] a = new int[length];
		for(int i=0; i<length; i++){
			a[i] = (int) (100*Math.random());
		}
		return a;
	}
	
	/**
	 * swap the value of elements
	 * @param a
	 * @param i
	 * @param j
	 */
	public static void swap(int[] a, int i, int j){
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp ;
	}
	
	/**
	 * Print Elements of Array
	 * @param a
	 */
	public static void printArrays(int[] a){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<a.length; i++){
			sb.append(a[i]).append(",");
		}
		System.out.println(sb.toString());
	}
	
}
