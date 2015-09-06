/*
 * Copyright Walker Studio
 * All Rights Reserved.
 * 
 * �ļ����ƣ� ManyTreeNode.java
 * ժ Ҫ��
 * �� �ߣ� Walker
 * ����ʱ�䣺 2013-03-19
 */
package com.test.zp.tblog;

import java.util.ArrayList;
import java.util.List;

/**
 * ������ڵ�
 *
 * @author Walker
 * @verion 1.0.0.0
 */
public class ManyTreeNode 
{
	/** ���ڵ�*/
	private TreeNode data;
	/** ��������*/
	private List<ManyTreeNode> childList;
	
	/**
	 * ���캯��
	 * 
	 * @param data ���ڵ�
	 */
	public ManyTreeNode(TreeNode data)
	{
		this.data = data;
		this.childList = new ArrayList<ManyTreeNode>();
	}
	
	/**
	 * ���캯��
	 * 
	 * @param data ���ڵ�
	 * @param childList ��������
	 */
	public ManyTreeNode(TreeNode data, List<ManyTreeNode> childList)
	{
		this.data = data;
		this.childList = childList;
	}

	public TreeNode getData() {
		return data;
	}

	public void setData(TreeNode data) {
		this.data = data;
	}

	public List<ManyTreeNode> getChildList() {
		return childList;
	}

	public void setChildList(List<ManyTreeNode> childList) {
		this.childList = childList;
	}

}
