package com.test.zp.sort;

public class QuickSort {
	
	public static void main(String[] args) {
		
		int[] a = CommonUtils.generateArray(20);
		
		
		CommonUtils.printArrays(a);
		
		quickSort(a, 0, a.length-1);
		
//		bubbleSort(a);
		
//		insertSort(a);
		
//		selectSort(a);

		CommonUtils.printArrays(a);
	}

	
	private static void quickSort(int[] a, int begin, int end) {
		
		if(begin>=end) return ;
		int tmp = a[begin];
		int i=begin, j=end;
		
		while(i < j){
			while(i<end && a[i]>=tmp) i++;
			while(j>begin && a[j]<=tmp) j--;
			if(i<j) CommonUtils.swap(a, i, j);
		}
		if(j!=begin)  CommonUtils.swap(a, begin, j);
		
		quickSort(a, begin, j-1);
		quickSort(a, j+1, end);
	}


	private static void selectSort(int[] a) {
		
		for(int i=0; i<a.length; i++){
			int k = i;
			for(int j=i; j<a.length; j++){
				if(a[j] < a[k]) k=j;
			}
			CommonUtils.swap(a, i, k);
		}
	}

	
	private static void insertSort(int[] a) {
		for(int i=1;i<a.length;i++){
			int tmp = a[i] ;
			int j;
			
			for(j=0;j<i;j++){
				if(a[i]<a[j]) break;
			}
			for(int k=i; k>j; k--){
				a[k] = a[k-1];
			}
			a[j] = tmp;
		}
	}



	private static void bubbleSort(int[] a) {
		for(int i=0; i<a.length;i++){
			for(int j=i+1;j<a.length;j++){
				if(a[i] >= a[j]) CommonUtils.swap(a, i, j);
			}
		}
	}

//	private static void quickSort(int[] a, int begin, int end) {
//		
//		if(begin >= end) return ;
//		
//		int tmp=a[begin];
//		int i=begin, j=end;
//		
//		while(i < j){
//			while(a[i] <= tmp && i<end) i++;
//			while(a[j] >= tmp && j>begin) j--;
//			if(i<j){
//				CommonUtils.swap(a, i, j);
//			}
//		}
//		
//		if(j!=begin) CommonUtils.swap(a, begin, j);
//		
//		quickSort(a, begin, j-1);
//		quickSort(a, j+1, end);
//		
//	}
	
}
