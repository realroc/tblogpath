package com.test.zp.tblog.mid;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class WeiboMessageNode {
	
	static DefaultMutableTreeNode treeNode;
	
	public static void addToTreeNode(WeiboMessage message){
		if(message == null) return ;
		if(message.getParentMid().equals(((WeiboMessage) treeNode.getUserObject()).getMid())){
			treeNode.add(new DefaultMutableTreeNode(message));
		} else {
			
			
			Enumeration<DefaultMutableTreeNode> enums = treeNode.breadthFirstEnumeration() ;
			
			while(enums.hasMoreElements()){
				DefaultMutableTreeNode dmtn = enums.nextElement();
				if(message.getParentMid().equals(((WeiboMessage) dmtn.getUserObject()).getMid())){
					dmtn.add(new DefaultMutableTreeNode(message));
					break;
				}
				
			}
			
		}
	}
	
	
	
	public static void main(String[] args) {
		
		WeiboMessage message = new WeiboMessage(null, "1");
		treeNode = new DefaultMutableTreeNode(message);
		
		addToTreeNode(new WeiboMessage("1","2"));
		addToTreeNode(new WeiboMessage("1","3"));
		addToTreeNode(new WeiboMessage("2","4"));
		addToTreeNode(new WeiboMessage("4","5"));
		addToTreeNode(new WeiboMessage("2","6"));
		addToTreeNode(new WeiboMessage("6","8"));
		addToTreeNode(new WeiboMessage("8","9"));
		addToTreeNode(new WeiboMessage("4","10"));
		addToTreeNode(new WeiboMessage("9","11"));
		
		
		DefaultMutableTreeNode leaf = treeNode.getFirstLeaf();
		while(leaf != null){
			StringBuffer sb = new StringBuffer();
			for(TreeNode ub: leaf.getPath()){
				
				DefaultMutableTreeNode d = (DefaultMutableTreeNode) ub;
				WeiboMessage wm = (WeiboMessage) d.getUserObject();
				
				sb.append(wm.getMid()).append("->");
			}
			System.out.println(sb.toString());
			leaf = leaf.getNextLeaf();
		
		}

	}
}

