package com.test.zp.sort;

public class SortTest {
	
	public static void main(String[] args) {

		int[] a = new int[10];
		
		for(int i=0; i<10; i++){
			a[i] = (int) (10*Math.random());
		}
		
		CommonUtils.toString(a);
		
		quickSort(a);
		CommonUtils.toString(a);
		
//		bubbleSort(a);
//		CommonUtils.toString(a);
		
//		insertSort(a);
//		CommonUtils.toString(a);
		
//		selectSort(a);
//		CommonUtils.toString(a);
		
	}
	
	private static int[] quickSort(int[] a) {
		
		return a;
	}

	/**
	 * Bubble Sort
	 * @param a
	 * @return
	 */
	public static int[] bubbleSort(int[] a){
		for(int i=0; i<10; i++){
			for(int j=i+1; j<10; j++){
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
		for(int i=1; i<10; i++){
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
		
		for(int i=0; i<10; i++){
			int k = i;
			for(int j=i; j<10; j++){
				if(a[j] > a[k]){
					k =j ;
				}
			}
			CommonUtils.swap(a, k, i);
		}
		
		return a;
	}
	
}
