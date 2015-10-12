package com.weibo.mapred.tblogpath.keynode;

import javax.swing.tree.DefaultMutableTreeNode;

public class TblogTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;
	
	private String root_mid;
	private String parent_mid;
	private String mid;
	private String time;
	private String uid;
	private String rootmid_trand_cnt_total;
	private String trand_cnt;
	private String contribute;
	private String user_level;

	public TblogTreeNode(String root_mid, String parent_mid, String mid, String time, String uid, String rootmid_trand_cnt_total, String trand_cnt, String contribute, String user_level){
		this.root_mid = root_mid;
		this.parent_mid = parent_mid;
		this.mid = mid;
		this.time = time;
		this.uid = uid;
		this.rootmid_trand_cnt_total = rootmid_trand_cnt_total;
		this.trand_cnt = trand_cnt;
		this.contribute = contribute ;
		this.user_level = user_level;
	}

	public String getRoot_mid() {
		return root_mid;
	}

	public void setRoot_mid(String root_mid) {
		this.root_mid = root_mid;
	}

	public String getParent_mid() {
		return parent_mid;
	}

	public void setParent_mid(String parent_mid) {
		this.parent_mid = parent_mid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRootmid_trand_cnt_total() {
		return rootmid_trand_cnt_total;
	}

	public void setRootmid_trand_cnt_total(String rootmid_trand_cnt_total) {
		this.rootmid_trand_cnt_total = rootmid_trand_cnt_total;
	}

	public String getTrand_cnt() {
		return trand_cnt;
	}

	public void setTrand_cnt(String trand_cnt) {
		this.trand_cnt = trand_cnt;
	}

	public String getContribute() {
		return contribute;
	}

	public void setContribute(String contribute) {
		this.contribute = contribute;
	}

	public String getUser_level() {
		return user_level;
	}

	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}

}
