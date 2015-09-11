package com.test.zp.tblog.mid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

public class BuildTest {
	
	
	public HashMap<String, ArrayList<String>> getFromFile(){
		
		HashMap<String, ArrayList<String>> midMap = new HashMap<>();
		try {
			FileReader fr = new FileReader(new File("C:\\Users\\zengpeng\\Downloads\\rootmid_3882920303634394")) ;
			BufferedReader br = new BufferedReader(fr);
			String line = null ;

			while((line = br.readLine()) != null){
				String[] wblog = line.split("\001");
				if(midMap.containsKey(wblog[1])){
					midMap.get(wblog[1]).add(wblog[2]);
				} else{
					ArrayList<String> l = new ArrayList<>();
					l.add(wblog[2]);
					midMap.put(wblog[1], l);
				}
			}
			fr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return midMap;
	}
	
	
	public void generateFromMap(HashMap<String, ArrayList<String>> midMap, String parentMid, WeiboMessageNode root){
		
//		WeiboMessageNode root = new WeiboMessageNode(null, rootmid) ;
		
		if(midMap == null) return  ;
		ArrayList<String> al = midMap.get(parentMid);
		if(al == null) return ;
		
		for(String mid:midMap.get(parentMid)){
			root.addToTreeNode(new WeiboMessage(parentMid, mid));
			generateFromMap(midMap, mid, root);
		}
	}
	
	
	public static void main(String[] args) {
		
		String rootmid = "3882920303634394" ;
		WeiboMessageNode root = new WeiboMessageNode(null, "3882920303634394") ;
		BuildTest bt = new BuildTest();
		bt.generateFromMap(bt.getFromFile(), rootmid, root);
		
		root.generatePath(root.treeNode);
	}
	
	
	
	
	
	
//	public static void main(String[] args) {
//		
//		WeiboMessageNode root = new WeiboMessageNode(null, "3882359008955070") ;
//		
//		try {
//			FileReader fr = new FileReader(new File("C:\\Users\\zengpeng\\Downloads\\000000_0")) ;
//			BufferedReader br = new BufferedReader(fr);
//			String line = null ;
//			
//			while((line = br.readLine()) != null){
//				String[] wblog = line.split("\t");
////				System.out.println(line + "----" + wblog[1] + "|" + wblog[2]);
//				root.addToTreeNode(new WeiboMessage(wblog[1], wblog[2]));
//			}
//			fr.close();
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//		
//		root.generatePath(root.treeNode);
//	}
}
