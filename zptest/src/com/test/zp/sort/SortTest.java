package com.test.zp.sort;

public class SortTest {
	
	public static void main(String[] args) {

		int[] a = CommonUtils.generateArray(20);
		
		CommonUtils.printArrays(a);
		
		bubbleSort(a);
		CommonUtils.printArrays(a);
		
//		insertSort(a);
//		CommonUtils.printArrays(a);
		
//		selectSort(a);
//		CommonUtils.printArrays(a);
		
	}

	/**
	 * Bubble Sort
	 * @param a
	 * @return
	 */
	public static int[] bubbleSort(int[] a){
		for(int i=0; i<a.length; i++){
			for(int j=i+1; j<a.length; j++){
				if(a[i] < a[j]){
					CommonUtils.swap(a, i, j);
				}
			}
		}
		return a;
	}
	
	
	
	/**
	 * InsertSort
	 * @param a
	 * @return
	 */
	public static int[] insertSort(int[] a){
		for(int i=1; i<a.length; i++){
			int k = i;
			for(int j=i; j>=0; j--){
				if(a[i] > a[j]){
					k=j;
				}
			}
			
			int temp = a[i];
			while(i>k){
				CommonUtils.swap(a, i, i-1);
				i--;
			}
			a[k] = temp ;
			
		}
		return a;
	}
	
	/**
	 * Select Sort
	 * @param a
	 * @return
	 */
	public static int[] selectSort(int[] a){
		
		for(int i=0; i<a.length; i++){
			int k = i;
			for(int j=i; j<a.length; j++){
				if(a[j] > a[k]){
					k =j ;
				}
			}
			CommonUtils.swap(a, k, i);
		}
		
		return a;
	}
	
}
