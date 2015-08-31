package com.test.zp.sort;

public class QuickSort {
	
	public static void main(String[] args) {
		
		int[] a = CommonUtils.generateArray(20);
		
		
		CommonUtils.printArrays(a);
		
		quickSort(a, 0, a.length);
		
		CommonUtils.printArrays(a);
		
	}
	
	
//	public static void quickSort(int[] a, int begin, int end){
//		
//		int mid = (begin + end) / 2;
//		quickSort(a, begin, mid-1);
//		quickSort(a, mid+1 , end );
//		
//	}
	
	

   private static void quickSort(int[] in,int begin, int end) {
        if( begin == end || begin == (end-1) ) return;
        int p = in[begin];
        int a = begin +1;
        int b = a;
        for( ; b < end; b++) {
            //�ö��������������ʵ��Comparable�ӿڣ���������ʹ��compareTo�������бȽ�
            if( in[b] > p) {
                if(a == b){a++; continue;}
                int temp = in[a];
                in[a] = in[b];
                in[b] = temp;
                a++;
            }
        }
        in[begin] = in[a-1];
        in[a-1] = p;
        if( a-1 > begin){
            quickSort(in,begin, a);
        } 
        if( end-1 > a ) {
            quickSort(in,a, end);
        } 
        return;
    }

}
