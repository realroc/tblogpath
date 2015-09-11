package com.test.zp.tblog.tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.tree.TreeNode;

public class TblogTreeTest {
	
	private final String LINE_SEPERATE_TAB = "\\|";
	private final String LINE_SEPERATE_HIVE = "\001";
//	public static void main(String[] args) {
//
//		TblogTreeNode rootNode = new TblogTreeNode(null, "3882920303634394");
//
//		System.out.println(System.currentTimeMillis());
//
//		int i = 0;
//		try {
//			FileReader fr = new FileReader(new File("C:\\Users\\zengpeng\\Downloads\\rootmid_3882920303634394"));
//			BufferedReader br = new BufferedReader(fr);
//			String line = null;
//
//			while ((line = br.readLine()) != null) {
//				String[] wblog = line.split("\001");
//				// System.out.println(line + "----" + wblog[1] + "|" +
//				// wblog[2]);
//				rootNode.addToTreeNode(new TblogTreeNode(wblog[1], wblog[2]));
//				// System.out.println(System.currentTimeMillis());
//				i++;
//				if (i % 10000 == 0)
//					System.out.println(i);
//			}
//			fr.close();
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println(System.currentTimeMillis());
//
//		rootNode.generatePath(rootNode);
//	}
	
	public static void main(String[] args) {
		
		String rootmid = "3882920303634394";
		TblogTreeTest t = new TblogTreeTest();
		TblogTreeNode rootNode = new TblogTreeNode(null, rootmid);
		System.out.println(System.currentTimeMillis());
		t.addToParentNode(rootmid, t.getFromFile(), rootNode);
		System.out.println(System.currentTimeMillis());
//		t.generatePath(rootNode);
		System.out.println(rootNode.getLevel());
		System.out.println(System.currentTimeMillis());
	}
	
	
//	public void genTree(String parentMid, HashMap<String, ArrayList<String>> midMap, TblogTreeNode treeNode){
//		TblogTreeNode rootNode = new TblogTreeNode(null, rootMid);
//		addToParentNode("3882359008955070", midMap, rootNode);
//	}
	
	
	public void addToParentNode(String parentMid, HashMap<String, ArrayList<String>> midMap, TblogTreeNode parentNode){
		if(parentMid == null || parentNode == null || midMap == null) return ;
		ArrayList<String> midList = midMap.get(parentMid);
		if(midList == null || midList.size() == 0) return ;
		for(String mid: midList){
			TblogTreeNode node = new TblogTreeNode(parentMid, mid);
			parentNode.add(node);
			addToParentNode(mid, midMap, node);
		}
		
	}
	
	
	public HashMap<String, ArrayList<String>> getFromFile(){
		
		HashMap<String, ArrayList<String>> midMap = new HashMap<>();
		try {
			FileReader fr = new FileReader(new File("C:\\Users\\zengpeng\\Downloads\\rootmid_3882920303634394")) ;
			BufferedReader br = new BufferedReader(fr);
			String line = null ;

			while((line = br.readLine()) != null && line.length() > 0){
				String[] wblog = line.split(LINE_SEPERATE_HIVE);
//System.out.println(line);				
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
		
	
	
	/**
	 * generate treePath from root to leaf
	 * @param dmtn
	 */
	public void generatePath(TblogTreeNode treeNode){
		
		TblogTreeNode leaf = (TblogTreeNode) treeNode.getFirstLeaf();
		
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(new File("C:\\Users\\zengpeng\\Downloads\\out")));
			
			while(leaf != null){
				StringBuffer sb = new StringBuffer();
				for(TreeNode ub: leaf.getPath()){
					
					TblogTreeNode d = (TblogTreeNode) ub;
					
					sb.append(d.getMid()).append("->");
					
				}
				
				bw.write(sb.toString());
				bw.newLine();
				bw.flush();

				leaf = (TblogTreeNode) leaf.getNextLeaf();
			
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
}
