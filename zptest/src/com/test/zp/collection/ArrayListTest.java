package com.test.zp.collection;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {
	
	static List<String> sList = new ArrayList<String>();
	
	public static void main(String[] args) {
//		ArrayList<String> al = new ArrayList<String> ();
//		
//		
//		Collections.sort(al);
//		new ArrayListTest().heapOutOfMemory();
		new ArrayListTest().stackOutOfMemory();

	}
	
	public void heapOutOfMemory(){
		while(true) sList.add("test");
	}
	
	public void stackOutOfMemory(){
		stackOutOfMemory();
	}
}
