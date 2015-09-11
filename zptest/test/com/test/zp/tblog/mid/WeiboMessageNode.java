package com.test.zp.tblog.mid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class WeiboMessageNode {
	
	DefaultMutableTreeNode treeNode;
	
	public WeiboMessageNode(String parentMid, String mid) {
		treeNode = new DefaultMutableTreeNode(new WeiboMessage(parentMid, mid));
	}

	public void addToTreeNode(WeiboMessage message){
		if(message == null) return ;
		
//		int addSuccess = 0 ;
		if(message.getParentMid().equals(((WeiboMessage) this.treeNode.getUserObject()).getMid())){
			this.treeNode.add(new DefaultMutableTreeNode(message));
//			addSuccess =1 ;
		} else {
			
			
			Enumeration<DefaultMutableTreeNode> enums = treeNode.breadthFirstEnumeration() ;
			
			while(enums.hasMoreElements()){
				DefaultMutableTreeNode dmtn = enums.nextElement();
				if(message.getParentMid().equals(((WeiboMessage) dmtn.getUserObject()).getMid())){
//					addSuccess = 1 ;
					dmtn.add(new DefaultMutableTreeNode(message));
					break;
				}
				
			}
			
//			if(addSuccess == 0) System.out.println("test:" + message.getMid());
		}
	}
	
	
	
	/**
	 * generate treePath from root to leaf
	 * @param dmtn
	 */
	public void generatePath(DefaultMutableTreeNode dmtn){
		
		DefaultMutableTreeNode leaf = this.treeNode.getFirstLeaf();
		
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(new File("C:\\Users\\zengpeng\\Downloads\\out")));
			
			while(leaf != null){
				StringBuffer sb = new StringBuffer();
				for(TreeNode ub: leaf.getPath()){
					
					DefaultMutableTreeNode d = (DefaultMutableTreeNode) ub;
					WeiboMessage wm = (WeiboMessage) d.getUserObject();
					
					sb.append(wm.getMid()).append("->");
					
				}
				
				bw.write(sb.toString());
				bw.newLine();
				bw.flush();

				leaf = leaf.getNextLeaf();
			
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	
	/*
	public static void main(String[] args) {
		
		long time1 = System.currentTimeMillis() ;
		try {
			FileReader fr = new FileReader(new File("C:\\Users\\zengpeng\\Downloads\\000000_0")) ;
			BufferedReader br = new BufferedReader(fr);
			String line = null ;
			
			while((line = br.readLine()) != null){
				String[] wblog = line.split("\t");
//				System.out.println(line + "----" + wblog[1] + "|" + wblog[2]);
				addToTreeNode(new WeiboMessage(wblog[1], wblog[2]));
			}
			fr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		long time2 = System.currentTimeMillis() ;
		//ParentMid需要先排好序
//		addToTreeNode(new WeiboMessage("1","2"));
//		addToTreeNode(new WeiboMessage("1","3"));
//		addToTreeNode(new WeiboMessage("2","4"));
//		addToTreeNode(new WeiboMessage("4","5"));
//		addToTreeNode(new WeiboMessage("2","6"));
//		addToTreeNode(new WeiboMessage("6","8"));
//		addToTreeNode(new WeiboMessage("8","9"));
//		addToTreeNode(new WeiboMessage("4","10"));
//		addToTreeNode(new WeiboMessage("9","11"));
		
		
		DefaultMutableTreeNode leaf = treeNode.getFirstLeaf();
	
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(new File("C:\\Users\\zengpeng\\Downloads\\output1")));
			
			while(leaf != null){
				StringBuffer sb = new StringBuffer();
				for(TreeNode ub: leaf.getPath()){
					
					DefaultMutableTreeNode d = (DefaultMutableTreeNode) ub;
					WeiboMessage wm = (WeiboMessage) d.getUserObject();
					
					sb.append(wm.getMid()).append("->");
					
				}
				
				bw.write(sb.toString());
				bw.newLine();
				bw.flush();

				leaf = leaf.getNextLeaf();
			
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		long time3 = System.currentTimeMillis() ;
		
		System.out.println(time1 + "|" + time2 + "|" + time3);
	}
	*/
}

