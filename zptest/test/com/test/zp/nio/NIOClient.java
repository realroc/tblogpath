package com.test.zp.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


public class NIOClient {
	
	public static void main(String[] args) {
		new NIOClient().run();
	}
	
	public void run(){
		SocketChannel sc ;
		Selector selector ;
		ByteBuffer buffer = ByteBuffer.allocate(1024) ;
		
		try {
			sc = SocketChannel.open();
			selector = Selector.open();
			sc.configureBlocking(false);
			sc.connect(new InetSocketAddress("localhost", 8888));
			sc.register(selector, SelectionKey.OP_CONNECT);
			
			while(true){
				selector.select();
				Iterator<SelectionKey> keyIt = selector.selectedKeys().iterator();
				while(keyIt.hasNext()){
					SelectionKey key = keyIt.next() ;
					if(key.isConnectable()){
						sc = (SocketChannel) key.channel() ;
						sc.finishConnect();
						buffer.clear();
						buffer.put("test".getBytes());
						buffer.flip();
						sc.write(buffer);
						sc.register(selector, SelectionKey.OP_READ);
					}if(key.isReadable()){
						sc = (SocketChannel) key.channel();
						buffer.clear();
						int i = sc.read(buffer);
						System.out.println(new String(buffer.array(),0,i));
						sc.register(selector, SelectionKey.OP_WRITE);
					}if(key.isWritable()){
						sc = (SocketChannel) key.channel() ;
						buffer.clear();
						buffer.put("writable test".getBytes());
						buffer.flip();
						sc.write(buffer);
						sc.register(selector, SelectionKey.OP_READ);
					}
					keyIt.remove();
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
