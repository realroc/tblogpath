package com.test.zp.nio.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import io.netty.channel.socket.nio.NioSocketChannel;

public class NIOClientTest2 {

	public static void main(String[] args) {
		for(int i=0; i<200; i++){
			new NIOClientTest2().connect();
		}
			
	}
	
	public void connect(){
		
		SocketChannel channel ;
		Selector selector;
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		try {
			channel = SocketChannel.open();
			selector = Selector.open();
			channel.configureBlocking(false);
			channel.connect(new InetSocketAddress("localhost", 8888));
			channel.register(selector, SelectionKey.OP_CONNECT);
			
			while(true){
				
				selector.select();
				
				Iterator<SelectionKey> keyIt = selector.selectedKeys().iterator();
				
				while(keyIt.hasNext()){
					SelectionKey key = keyIt.next();
					if(key.isConnectable()){
						channel = (SocketChannel) key.channel();
						channel.finishConnect();
						buffer.clear();
						buffer.put("test".getBytes());
						buffer.flip();
						channel.write(buffer);
						channel.register(selector, SelectionKey.OP_READ);
					}else if(key.isReadable()){
						channel = (SocketChannel) key.channel();
						buffer.clear();
						int i = channel.read(buffer);
						System.out.println(new String(buffer.array(), 0, i));
						channel.register(selector, SelectionKey.OP_WRITE);
					}else if(key.isWritable()){
						channel = (SocketChannel) key.channel();
						buffer.clear();
						buffer.put("Writable Key".getBytes());
						buffer.flip();
						channel.write(buffer);
						channel.close();
						return ;
//						channel.register(selector, SelectionKey.OP_READ);
					}
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
