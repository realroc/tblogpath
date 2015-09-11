package com.test.zp.tblog.tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TblogTreeTest {
	
	
	
	
	public static void main(String[] args) {
	
	TblogTreeNode rootNode = new TblogTreeNode(null, "3882920303634394") ;

System.out.println(System.currentTimeMillis());	
	
	int i =0 ;
	try {
		FileReader fr = new FileReader(new File("C:\\Users\\zengpeng\\Downloads\\rootmid_3882920303634394")) ;
		BufferedReader br = new BufferedReader(fr);
		String line = null ;
		
		while((line = br.readLine()) != null){
			String[] wblog = line.split("\001");
//			System.out.println(line + "----" + wblog[1] + "|" + wblog[2]);
			rootNode.addToTreeNode(new TblogTreeNode(wblog[1], wblog[2]));
//			System.out.println(System.currentTimeMillis());	
			i++;
			if(i % 10000 == 0) System.out.println(i); 
		}
		fr.close();
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} 

System.out.println(System.currentTimeMillis());	
	
	rootNode.generatePath(rootNode);
}
}
