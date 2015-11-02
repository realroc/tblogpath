package com.test.zp.nio;

import java.util.ArrayList;

public class HappyNum {
    
	public static void main(String[] args) {
		System.out.println(new HappyNum().isHappy(3));
	}
    public boolean isHappy(int n) {
        
        ArrayList<Integer> retList = new ArrayList();
        
        int sum = 0 ;
        
        while(n >= 1){
            sum = sum + (n%10)*(n%10)  ;
            n = n/10 ;
            
            if(n < 1){
                if(sum == 1){
                    return true ;
                }else{
                    if(retList.contains(sum)) return false;
                    retList.add(sum);
                    n=sum ;
                    sum = 0 ;
                    }
                }
        }
        return false ;  
    }
}