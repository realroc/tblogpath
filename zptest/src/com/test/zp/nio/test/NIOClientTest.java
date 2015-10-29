package com.test.zp.nio.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClientTest {

	public static void main(String[] args) {
//		NIOClientTest nct = 
		for(int i=0; i<= 10; i++){
			new NIOClientTest().connect();
		}
	}
	
	public void connect(){
		try {
			Selector selector = Selector.open();
			SocketChannel sc = SocketChannel.open();
			
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_CONNECT);
			sc.connect(new InetSocketAddress("localhost", 8888)); 
			
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			
			while(true){
				selector.select();
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				
				while(it.hasNext()){
					SelectionKey key = it.next();
					if(key.isConnectable()){
						sc = (SocketChannel) key.channel();
	                    if (sc.isConnectionPending()) {  
	                        sc.finishConnect();  
	                        System.out.println("完成连接!");  
	                        buffer.clear();  
	                        buffer.put("Hello,Server hh".getBytes());  
	                        buffer.flip();  
	                        sc.write(buffer);  
	                    }  
						sc.register(selector, SelectionKey.OP_READ);
					}else if(key.isReadable()){
						sc = (SocketChannel) key.channel();
						buffer.clear();
						int i = sc.read(buffer);
						System.out.println(new String(buffer.array(), 0, i));
//						sc.register(selector, SelectionKey.OP_WRITE);
						sc.close();
						return;
//						System.exit(0);
					}
//					else if(key.isWritable()){
//						sc = (SocketChannel) key.channel();
//						buffer.clear();
//						buffer.put("client".getBytes());
//						buffer.flip();
//						sc.write(buffer);
//						sc.register(selector, SelectionKey.OP_READ);
//					}
				}
				it.remove();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
}
