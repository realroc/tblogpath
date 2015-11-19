package com.test.zp.sort;

public class QuickSort {
	
	public static void main(String[] args) {
		
		int[] a = CommonUtils.generateArray(30);
		
		CommonUtils.printArrays(a);
		
		int[] b = a.clone();
 		quickSort(b, 0, b.length-1);
		System.out.println("quickSort:");
		CommonUtils.printArrays(b);
		
		int[] b1 = a.clone();
		bubbleSort(b1);
		System.out.println("bubbleSort:");
		CommonUtils.printArrays(b1);
		
		int[] b2 = a.clone();
		insertSort(b2);
		System.out.println("insertSort:");
		CommonUtils.printArrays(b2);
		
		int[] b3 = a.clone();
		selectSort(b3);
		System.out.println("selectSort:");
		CommonUtils.printArrays(b3);
		
	}

	private static void selectSort(int[] a) {
		for(int i=0; i<a.length; i++){
			int k = i;
			for(int j=i+1; j<a.length; j++){
				if(a[k] > a[j]) k = j;
			}
			CommonUtils.swap(a, i, k);
		}
	}

	private static void insertSort(int[] a) {
		
		for(int i=1; i<a.length; i++){
			int k=i;
			for(int j=0; j<i; j++){
				if(a[i] < a[j]){
					k=j;
					break;
				}
			}
			int tmp = a[i] ;
			for(int j=i; j>k; j--){
				CommonUtils.swap(a, j, j-1);
			}
			a[k] = tmp ;
			
		} 	
	}

	
	private static void bubbleSort(int[] a) {
		for(int i=a.length-1; i>0; i--){
			for(int j=0; j<i; j++){
				if(a[j] > a[j+1]) CommonUtils.swap(a, j, j+1);
			}
		}
	}

	
	private static void quickSort(int[] a, int min, int max) {
		
		if(min >= max) return ;
		int tmp = a[min] ;
		int i = min, j = max ;
		
		while(i<j){
			while(i<max && a[i] <= tmp) i++;
			while(j>min && a[j] >= tmp) j--;
			if(i<j) CommonUtils.swap(a, i, j);
		}
		
		if(j!=min) CommonUtils.swap(a, min, j);
		
		quickSort(a, min, j-1);
		quickSort(a, j+1, max);
		
	}
}
