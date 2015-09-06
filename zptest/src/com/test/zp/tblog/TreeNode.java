/*
 * Copyright Walker Studio
 * All Rights Reserved.
 * 
 * �ļ����ƣ� TreeNode.java
 * ժ Ҫ��
 * �� �ߣ� Walker
 * ����ʱ�䣺 2013-03-19
 */
package com.test.zp.tblog;

/**
 * ���ڵ�
 * 
 * @author Walker
 * @version 1.0.0.0
 */
public class TreeNode 
{
	/** �ڵ�Id*/
	private String nodeId;
	/** ���ڵ�Id*/
	private String parentId;
	/** �ı�����*/
	private String text;
	
	/**
	 * ���캯��
	 * 
	 * @param nodeId �ڵ�Id
	 */
	public TreeNode(String nodeId)
	{
		this.nodeId = nodeId;
	}
	
	/**
	 * ���캯��
	 * 
	 * @param nodeId �ڵ�Id
	 * @param parentId ���ڵ�Id
	 */
	public TreeNode(String nodeId, String parentId)
	{
		this.nodeId = nodeId;
		this.parentId = parentId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
