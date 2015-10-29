package com.test.zp.nio.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class NIOServerTest2 {
	
	Logger logger = Logger.getLogger(NIOServerTest2.class);
	public static void main(String[] args) {
		NIOServerTest2 nct = new NIOServerTest2();
		nct.connect();
	}
	
	public void connect(){
		ServerSocketChannel ssc ;
		SocketChannel sc ;
		Selector selector ;
		ByteBuffer buffer = ByteBuffer.allocate(1024) ;
		
		try{
			
			
			ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.bind(new InetSocketAddress("127.0.0.1", 1111));
			
			sc = SocketChannel.open() ;
			selector = Selector.open() ;
			
//			sc.configureBlocking(false);
//			sc.connect(new InetSocketAddress("127.0.0.1", 8888));
//			sc.register(selector, SelectionKey.OP_CONNECT);
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			
			for(;;){
				selector.select();
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				
				while(it.hasNext()){
					
					SelectionKey key = it.next();
					
					if(key.isAcceptable()){
						sc = ssc.accept();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					} else if(key.isReadable()){
						sc = (SocketChannel) key.channel();
						buffer.clear();
						int i = sc.read(buffer);
						System.out.println("recieve message from client:" + new String(buffer.array(), 0, i));
						sc.register(selector, SelectionKey.OP_WRITE);
					} else if(key.isWritable()){
						sc = (SocketChannel) key.channel() ;
						buffer.clear();
						buffer.put("writable message".getBytes());
						buffer.flip();
						sc.write(buffer);
						sc.register(selector, SelectionKey.OP_READ);
					}
					
					it.remove();
				}
			}
			
		} catch(Exception e){
//			logger.debug(e);
			e.printStackTrace();
		}
	}
}
