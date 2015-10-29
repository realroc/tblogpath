package com.test.zp.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileReaderTest {
	
	public static void main(String[] args) {
		
		HashMap<String, ArrayList<String>> midMap = new HashMap<>();
		try {
			FileReader fr = new FileReader(new File("C:\\Users\\zengpeng\\Downloads\\rootmid_3882920303634394")) ;
			BufferedReader br = new BufferedReader(fr);
			String line = null ;
//			int i=0;
//			System.out.println(System.currentTimeMillis());
			while((line = br.readLine()) != null){
				String[] wblog = line.split("\001");
//				System.out.println("lin1:" + wblog[0]);
//				i++;
//				System.out.println("lin1:" + wblog[0] + " line2:" + wblog[1] + " line3:" + wblog[2]);
				if(midMap.containsKey(wblog[1])){
					midMap.get(wblog[1]).add(wblog[2]);
				} else{
					ArrayList<String> l = new ArrayList<>();
					l.add(wblog[2]);
					midMap.put(wblog[1], l);
				}
				
//				break;
			}
//			System.out.println(System.currentTimeMillis());
//			System.out.println(i);
			
			System.out.println(midMap.get("3882974506747769").size());
			fr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
