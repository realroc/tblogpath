package com.weibo.mapred.tblogpath.keynode;

public class NodeInfo {
	
	private static final String SPLIT = "\t";
	
	private String rootmid;
	private String parentmid;
	private String mid;
	private String time;
	private String uid;
	private String rootmid_trand_cnt_total;
	private String trand_cnt;
	private String contribute;
	private String level;
	
	public NodeInfo(String rootmid, String parentmid, String mid, String time, String uid, String rootmid_trand_cnt_total, String trand_cnt, String contribute, String level){
		this.rootmid = rootmid;
		this.parentmid = parentmid;
		this.mid = mid;
		this.time = time;
		this.uid = uid;
		this.rootmid_trand_cnt_total = rootmid_trand_cnt_total;
		this.trand_cnt = trand_cnt;
		this.contribute = contribute ;
		this.level = level;
	}
	
	public String getRootmid() {
		return rootmid;
	}
	public void setRootmid(String rootmid) {
		this.rootmid = rootmid;
	}
	public String getParentmid() {
		return parentmid;
	}
	public void setParentmid(String parentmid) {
		this.parentmid = parentmid;
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
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		//sb.append(mid).append(SPLIT).append(rootmid).append(SPLIT).append(parentmid).append(SPLIT).append(trand_cnt).append(SPLIT)
		//	.append(rootmid_trand_cnt_total).append(SPLIT).append(time).append(SPLIT).append(uid).append(SPLIT).append(contribute).append(SPLIT).append(level);
		sb.append(mid).append(SPLIT).append(uid).append(SPLIT).append(SPLIT).append(time).append(parentmid).append(SPLIT).append(trand_cnt).append(SPLIT)
			.append(rootmid_trand_cnt_total).append(SPLIT).append(contribute).append(SPLIT).append(level);
				
		return sb.toString();
	}
	
}
