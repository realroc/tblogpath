package com.weibo.mapred.tblogpath;

import javax.swing.tree.DefaultMutableTreeNode;

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

}
