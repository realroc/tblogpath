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
	
	ServerSocketChannel ssc;
	Selector selector;
	SocketChannel sc;
	
	public static void main(String[] args) throws IOException {
		new NIOServer().init();
	}
	
	public void init() throws IOException{
		selector = Selector.open();
		ssc = ServerSocketChannel.open();
		ssc.bind(new InetSocketAddress("localhost", 1111));
		ssc.configureBlocking(false);
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		
		while(true){
			selector.select();
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			
			while(it.hasNext()){
				SelectionKey key = it.next();
				it.remove();
				
				if(key.isAcceptable()){
					sc = ssc.accept();
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ);
				}else if(key.isReadable()){
					sc = (SocketChannel) key.channel();
					ByteBuffer bb = ByteBuffer.allocate(1024);
					bb.clear();
					int i = sc.read(bb);
					System.out.println(new String(bb.array(),0, i));
					sc.register(selector, SelectionKey.OP_WRITE);
				}else if(key.isWritable()){
					sc = (SocketChannel) key.channel();
					ByteBuffer bb = ByteBuffer.allocate(1024);
					bb.put("from server".getBytes());
					bb.flip();
					sc.write(bb);
					sc.register(selector, SelectionKey.OP_READ);
				}
			}
		}
	}
}