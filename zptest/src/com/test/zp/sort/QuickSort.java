package com.test.zp.sort;

public class QuickSort {
	
	public static void main(String[] args) {
		
		int[] a = CommonUtils.generateArray(50);
		
		
		CommonUtils.printArrays(a);
		
//		quickSort(a, 0, a.length-1);
		
//		bubbleSort(a);
		
//		insertSort(a);
		
		selectSort(a);

		CommonUtils.printArrays(a);
	}

	private static void selectSort(int[] a) {
		
		for(int i=0; i<a.length; i++){
			for(int j=i+1; j<a.length; j++){
				if(a[i] > a[j]) CommonUtils.swap(a, i, j);
			}
		}
	}

	
	private static void insertSort(int[] a) {
		
		for(int i=1; i<a.length; i++){
			int k=i;
			int tmp = a[i];
			for(int j=0; j<i; j++){
				if(a[i] < a[j]) {
					k=j;
					break;
				}
			}
			if(k!=i){
				for(int j=i;j>k;j--){
					a[j] = a[j-1];
				}
				a[k] = tmp;
			}
		}
	}

//	private static void bubbleSort(int[] a) {
//		for(int i=a.length-1; i>0; i--){
//			for(int j=0; j<i; j++){
//				if(a[j] > a[j+1]) CommonUtils.swap(a, j, j+1);
//			}
//		}
//	}

//	private static void quickSort(int[] a, int min, int max) {
//		
//		if(min>=max) return;
//		int tmp = a[min];
//		int i=min, j=max ;
//		
//		while(i<j){
//			while(i<max && a[i]<=tmp) i++ ;
//			while(j>min && a[j]>=tmp) j-- ;
//			if(i<j){
//				CommonUtils.swap(a, i, j);
//			}
//		}
//		if(j!=min) CommonUtils.swap(a, min, j);
//		
//		quickSort(a, min, j-1);
//		quickSort(a, j+1, max);
//	}

	
	
	
}
