package com.test.zp.nio.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClientTest2 {

	public static void main(String[] args) {
			new NIOClientTest2().connect();
	}
	
	public void connect(){
		
		SocketChannel sc ;
		Selector selector ;
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		try{
			sc = SocketChannel.open();
			selector = Selector.open();
			sc.configureBlocking(false);
			sc.connect(new InetSocketAddress("localhost", 8888));
			sc.register(selector, SelectionKey.OP_CONNECT);
			
			for(;;){
				selector.select();
System.out.println("key info:");					
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while(keys.hasNext()){
					SelectionKey key = keys.next();
				
					if(key.isConnectable()){
						sc = (SocketChannel) key.channel();
						sc.finishConnect();
						buffer.clear();
						buffer.put("Hello from client".getBytes());
						buffer.flip();
						sc.write(buffer);
						sc.register(selector, SelectionKey.OP_READ);
					} else if(key.isReadable()){
						sc = (SocketChannel) key.channel();
						buffer.clear();
						int length = sc.read(buffer);
						System.out.println(new String(buffer.array(), 0, length));
						sc.register(selector, SelectionKey.OP_WRITE);
					} else if(key.isWritable()){
						sc = (SocketChannel) key.channel();
						buffer.clear();
						buffer.put("Hello from client".getBytes());
						buffer.flip();
						sc.write(buffer);
						sc.register(selector, SelectionKey.OP_READ);
					}
					keys.remove();
				}
			}
			
		} catch(Exception e){
			
		}
	}
}
