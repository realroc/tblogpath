package com.test.zp.tblog.mid;

public class WeiboMessage {
	
	private String parentMid;
	
	private String mid;
	
	public WeiboMessage(String parentMid, String mid){
		this.parentMid = parentMid ;
		this.mid = mid ;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getParentMid() {
		return parentMid;
	}

	public void setParentMid(String parentMid) {
		this.parentMid = parentMid;
	}
}
