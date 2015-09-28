package com.test.zp.nio.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer{
	
	Selector selector = null;
	ServerSocketChannel ssc ;
	
	
	public void initServer(){
		
		try {
			selector = Selector.open();
			ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.bind(new InetSocketAddress("localhost", 1111));
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			handle();
		} catch (IOException e){ 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handle() throws IOException{
		
		SocketChannel sc ;
		while(true){
			System.out.println("tet1");				
			selector.select(0);
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			
			while(it.hasNext()){
				SelectionKey key = it.next();
				it.remove();
System.out.println("tet2");				
				if(key.isAcceptable()){
					ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
					sc = ssc.accept();
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ);
				} else if(key.isReadable()){
					sc = (SocketChannel) key.channel();
					ByteBuffer bbuf = ByteBuffer.allocate(1024);
					bbuf.clear();
					int t = sc.read(bbuf);
					System.out.println(new String(bbuf.array(), 0, t));
					sc.register(selector, SelectionKey.OP_WRITE);
				} else if(key.isWritable()){
					sc = (SocketChannel) key.channel();
					ByteBuffer bbuf = ByteBuffer.allocate(1024);
					bbuf.put("hello hello".getBytes());
					bbuf.flip();
					sc.write(bbuf);
					sc.register(selector, SelectionKey.OP_READ);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		NIOServer ns = new NIOServer();
		ns.initServer();
	}
}