package com.weibo.mapred.tblogpath.keynode;

import javax.swing.tree.DefaultMutableTreeNode;

public class TblogTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

	private String parentMid;

	private NodeInfo nodeInf;

	public TblogTreeNode(String parentMid, NodeInfo nodeInf) {
		this.parentMid = parentMid;
		this.nodeInf = nodeInf;
	}

	public String getParentMid() {
		return parentMid;
	}

	public void setParentMid(String parentMid) {
		this.parentMid = parentMid;
	}

	public NodeInfo getNodeInf() {
		return nodeInf;
	}

	public void setNodeInf(NodeInfo nodeInf) {
		this.nodeInf = nodeInf;
	}

}
