package com.test.zp.tblog.mid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BuildTest {
	
	
	
	
	public static void main(String[] args) {
		
		WeiboMessageNode root = new WeiboMessageNode(null, "3882359008955070") ;
		
		try {
			FileReader fr = new FileReader(new File("C:\\Users\\zengpeng\\Downloads\\000000_0")) ;
			BufferedReader br = new BufferedReader(fr);
			String line = null ;
			
			while((line = br.readLine()) != null){
				String[] wblog = line.split("\t");
//				System.out.println(line + "----" + wblog[1] + "|" + wblog[2]);
				root.addToTreeNode(new WeiboMessage(wblog[1], wblog[2]));
			}
			fr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
		
		
		root.generatePath(root.treeNode);
	}
}
