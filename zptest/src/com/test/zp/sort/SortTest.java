package com.test.zp.sort;

public class SortTest {
	
	public static void main(String[] args) {

		int[] a = CommonUtils.generateArray(20);
		
		CommonUtils.printArrays(a);
		
//		quickSort(a, 0, a.length-1);
		
//		bubbleSort(a);
		
//		insertSort(a);

		selectSort(a);
		CommonUtils.printArrays(a);
		
	}

	
	/**
	 * quick Sort
	 * @param a
	 * @return
	 */
	public static void quickSort(int[] a, int begin, int end){
		if(begin >= end) return ;
		int base = a[begin];
		int i = begin, j = end ;
		while(i<j){
			while(i < end && a[i] <= base) i++;
			while(j > begin && a[j] >= base) j-- ;
			if(i<j) CommonUtils.swap(a, i, j);
		}
		if(j != begin) CommonUtils.swap(a, begin, j);
		quickSort(a, begin, j-1);
		quickSort(a, j+1, end);
	}
	
	

	/**
	 * Bubble Sort
	 * @param a
	 * @return
	 */
	private static void bubbleSort(int[] a) {
		for(int i=0; i<a.length; i++){
			for(int j=i; j<a.length; j++){
				if(a[i] > a[j]) CommonUtils.swap(a, i, j);
			}
		}
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
