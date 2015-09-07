/*
 * Copyright Walker Studio
 * All Rights Reserved.
 * 
 * �ļ����ƣ� ManyNodeTree.java
 * ժ Ҫ��
 * �� �ߣ� Walker
 * ����ʱ�䣺 2013-03-19
 */
package com.test.zp.tblog;

import java.util.ArrayList;
import java.util.List;

/**
 * ��������ɡ���������
 * 
 * @author Walker
 * @version 1.0.0.0
 */
public class ManyNodeTree 
{
	/** ����*/
	private ManyTreeNode root;
	
	/**
	 * ���캯��
	 */
	public ManyNodeTree()
	{
		root = new ManyTreeNode(new TreeNode("root"));
	}
	
	/**
	 * ����һ�Ŷ���������ڵ�Ϊroot
	 * 
	 * @param treeNodes ���ɶ�����Ľڵ㼯��
	 * @return ManyNodeTree
	 */
	public ManyNodeTree createTree(List<TreeNode> treeNodes)
	{
		if(treeNodes == null || treeNodes.size() < 0)
			return null;
		
		ManyNodeTree manyNodeTree =  new ManyNodeTree();
		
		//�����нڵ���ӵ��������
		for(TreeNode treeNode : treeNodes)
		{
			if(treeNode.getParentId().equals("root"))
			{
				//������һ���ڵ�
				manyNodeTree.getRoot().getChildList().add(new ManyTreeNode(treeNode));
			}
			else
			{
				addChild(manyNodeTree.getRoot(), treeNode);
			}
		}
		
		return manyNodeTree;
	}
	
	/**
	 * ��ָ��������ڵ�����ӽڵ�
	 * 
	 * @param manyTreeNode ������ڵ�
	 * @param child �ڵ�
	 */
	public void addChild(ManyTreeNode manyTreeNode, TreeNode child)
	{
		for(ManyTreeNode item : manyTreeNode.getChildList())
		{
			if(item.getData().getNodeId().equals(child.getParentId()))
			{
				//�ҵ���Ӧ�ĸ���
				item.getChildList().add(new ManyTreeNode(child));
				break;
			}
			else
			{
				if(item.getChildList() != null && item.getChildList().size() > 0)
				{
					addChild(item, child);
				}				
			}
		}
	}
	
	/**
	 * ��������� 
	 * 
	 * @param manyTreeNode ������ڵ�
	 * @return 
	 */
	public String iteratorTree(ManyTreeNode manyTreeNode)
	{
		StringBuilder buffer = new StringBuilder();
		buffer.append("\n");
		
		if(manyTreeNode != null) 
		{	
			for (ManyTreeNode index : manyTreeNode.getChildList()) 
			{
				buffer.append(index.getData().getNodeId()+ ",");
				
				if (index.getChildList() != null && index.getChildList().size() > 0 ) 
				{	
					buffer.append(iteratorTree(index));
				}
			}
		}
		
		buffer.append("\n");
		
		return buffer.toString();
	}
	
	public ManyTreeNode getRoot() {
		return root;
	}

	public void setRoot(ManyTreeNode root) {
		this.root = root;
	}
	
	public static void main(String[] args)
	{
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
			treeNodes.add(new TreeNode("ϵͳȨ�޹���", "root"));
			treeNodes.add(new TreeNode("�û�����", "ϵͳȨ�޹���"));
			treeNodes.add(new TreeNode("��ɫ����", "ϵͳȨ�޹���"));
			treeNodes.add(new TreeNode("�����", "ϵͳȨ�޹���"));
			treeNodes.add(new TreeNode("�û��˵�����", "ϵͳȨ�޹���"));
			treeNodes.add(new TreeNode("��ɫ�˵�����", "ϵͳȨ�޹���"));
			treeNodes.add(new TreeNode("�û�Ȩ�޹���", "ϵͳȨ�޹���"));
			treeNodes.add(new TreeNode("վ����", "root"));
			treeNodes.add(new TreeNode("д��", "վ����"));
			treeNodes.add(new TreeNode("����", "վ����"));
			treeNodes.add(new TreeNode("�ݸ�", "վ����"));
			
			
//		treeNodes.add(new TreeNode("2", "root"));
//		treeNodes.add(new TreeNode("3", "root"));
//		treeNodes.add(new TreeNode("5", "2"));
//		treeNodes.add(new TreeNode("4", "2"));
//		treeNodes.add(new TreeNode("7", "5"));
//		treeNodes.add(new TreeNode("9", "7"));
//		treeNodes.add(new TreeNode("11", "3"));
//		treeNodes.add(new TreeNode("12", "11"));

			
			ManyNodeTree tree = new ManyNodeTree();
			
			System.out.println(tree.iteratorTree(tree.createTree(treeNodes).getRoot()));
	}
	
}
