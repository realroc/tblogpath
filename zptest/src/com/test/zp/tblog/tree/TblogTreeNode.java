package com.test.zp.tblog.tree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class TblogTreeNode extends DefaultMutableTreeNode{

	private static final long serialVersionUID = 1L;

	private String parentMid;
	
	private String mid;
	
	public TblogTreeNode(String parentMid, String mid){
		this.parentMid = parentMid ;
		this.mid = mid ;
	}
	
	public String getParentMid() {
		return parentMid;
	}

	public void setParentMid(String parentMid) {
		this.parentMid = parentMid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public void addToTreeNode(TblogTreeNode tblogTreeNode) {
		if(tblogTreeNode == null) return ;
		
		if(tblogTreeNode.getParentMid().equals(this.mid)){
			this.add(tblogTreeNode);
//			addSuccess =1 ;
		} else {
			Enumeration<TblogTreeNode> enums = this.breadthFirstEnumeration() ;
			while(enums.hasMoreElements()){
				TblogTreeNode dmtn = enums.nextElement();
				if(tblogTreeNode.getParentMid().equals(dmtn.getMid())){
//					addSuccess = 1 ;
					dmtn.add(tblogTreeNode);
					break;
				}
			}
//			if(addSuccess == 0) System.out.println("test:" + message.getMid());
		}
	}
		

	public void generatePath(TblogTreeNode rootNode) {
		
		TblogTreeNode leaf = (TblogTreeNode) rootNode.getFirstLeaf();
		
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
