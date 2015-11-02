package com.test.zp.nio;

public class MoveZeroes {
	
	public static void main(String[] args) {
		
		
		int[] nums = {0,0,2};
		new MoveZeroes().moveZeroes(nums);
		
		for(int i=0; i< nums.length; i++){
			System.out.println(nums[i]);
		}
		
	}
	
	
    public void moveZeroes(int[] nums) {
        
    	boolean repeat = false ;
    	int count = 0;
        for(int i=0; i<nums.length; i++){
            count++;
        	if(repeat){
        		i--;
        		repeat = false ;
        	}
        	if(count == nums.length) break;
            if(nums[i] == 0) {
            	if((i+1 < nums.length) && nums[i+1] == 0)  repeat = true;
                for(int j=i;j<nums.length-1;j++){
                    nums[j] = nums[j+1];
                }
                nums[nums.length -1] = 0;
                
            }
            
        }
    }
	
}
