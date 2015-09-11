package com.test.zp.tblog;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class DefaultMutableTreeNodeTest {
	
	public static void main(String[] args) {
		
		
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		DefaultMutableTreeNode  node1 = new DefaultMutableTreeNode ("node1"); 
		DefaultMutableTreeNode  node2 = new DefaultMutableTreeNode ("node2"); 
		DefaultMutableTreeNode  node3 = new DefaultMutableTreeNode ("node3"); 
		
		root.add(node1);
		node1.add(node2);
		node1.add(node3);
		
		
	
		
//		for(TreeNode tn: node3.getPath()){
//			System.out.print(tn.toString() + "->");
//		}
		
		DefaultMutableTreeNode leaf = root.getFirstLeaf();
		while(leaf != null){
			StringBuffer sb = new StringBuffer();
			for(TreeNode ub: leaf.getPath()){
				sb.append(ub.toString()).append("->");
			}
			System.out.println(sb.toString());
			leaf = leaf.getNextLeaf();
		
		}
		
	}
}


//1 1  2
//1 1  3
//1 2  4
//1 2  5
//1 5  7
//1 7  9
//1 10  11
//1 11 12
