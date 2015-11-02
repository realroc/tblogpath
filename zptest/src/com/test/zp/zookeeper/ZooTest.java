package com.test.zp.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZooTest {
	
	public static void main(String[] args) {
		
	
		final int CLIENT_PORT = 4180;
		final int CONNECTION_TIMEOUT = 30;
		
		try{
			ZooKeeper zk = new ZooKeeper("127.0.0.1:" + CLIENT_PORT, 
			        CONNECTION_TIMEOUT, new Watcher() { 
			            // ������б��������¼�
			            public void process(WatchedEvent event) { 
			                System.out.println("�Ѿ�������" + event.getType() + "�¼���"); 
			            } 
			        }); 
//			zk.create("/testRootPath/test1", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE,
//					   CreateMode.PERSISTENT);
//			zk.setData("/testRootPath", "just 2 just for test".getBytes(), -1);
			
			System.out.println(new String(zk.getData("/testRootPath", null, new Stat())) );
			zk.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
