package com.test.zp.nio.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServerTest {
	
	
	public void init(){
		ServerSocketChannel ssc ;
		SocketChannel sc ;
		Selector selector ;
		ByteBuffer buffer = ByteBuffer.allocate(2048) ;
//		SelectionKey key ;
		
		try {
			ssc = ServerSocketChannel.open();
			selector = Selector.open();
			ssc.configureBlocking(false);
			ssc.bind(new InetSocketAddress("localhost", 1111));
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			
			while(true){
				selector.select();
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey key = it.next();
					if(key.isAcceptable()){
						sc = ssc.accept();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					}else if(key.isReadable()){
						sc = (SocketChannel) key.channel() ;
						buffer.clear();
						int i = sc.read(buffer);
						System.out.println(new String(buffer.array(), 0, i));
						sc.register(selector, SelectionKey.OP_WRITE);
					}else if(key.isWritable()){
						sc = (SocketChannel) key.channel();
						buffer.clear();
						buffer.put("test 123".getBytes());
						buffer.flip();
						sc.write(buffer);
						sc.register(selector, SelectionKey.OP_READ);
					}
					it.remove();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		NIOServerTest nst = new NIOServerTest();
		nst.init();
	}

}
