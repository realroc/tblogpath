package com.weibo.mapred.tblogpath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import com.hadoop.compression.lzo.LzopCodec;;;

/**
 * 博文传播路径生成
 * @author zengpeng
 * 2015年9月11日 下午5:23:29
 */
public class TblogPathGenMR {

	public static class RootMidMapper extends Mapper<Object, Text, Text, Text> {

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String[] mids = value.toString().split("\001");
			context.write(new Text(mids[0]), value);
		}
	}

	public static class TreeInfoReducer extends Reducer<Text, Text, Text, Text> {
		
		HashMap<String, ArrayList<String>> midMap = new HashMap<String, ArrayList<String>>();
		
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			
			for(Text midPair:values){
				if(midPair == null) continue ;
				String[] mids = midPair.toString().split("\001");
				if(mids == null || mids.length != 7) continue ;				
				if(midMap.containsKey(mids[1])){
					midMap.get(mids[1]).add(mids[2]);
				} else{
					ArrayList<String> al = new ArrayList<String>();
					al.add(mids[2]);
					midMap.put(mids[1], al);
				}
			}
			
			String rootmid = key.toString();
			TblogTreeNode rootNode = new TblogTreeNode(rootmid, rootmid);
			addToParentNode(rootmid, midMap, rootNode);
			Enumeration<TblogTreeNode> nodeEnum = rootNode.breadthFirstEnumeration();
			
			while(nodeEnum.hasMoreElements()){
				TblogTreeNode node = nodeEnum.nextElement();
				StringBuffer sb = new StringBuffer();
				sb.append(node.getMid()).append("\t").append(node.getParentMid()).append("\t").append(node.getChildCount()).append("\t").append(node.getLevel());
				context.write(key, new Text(sb.toString()));
			}
		}
		
		/**
		 * build path tree
		 * @param parentMid
		 * @param midMap
		 * @param parentNode
		 */
		public void addToParentNode(String parentMid, HashMap<String, ArrayList<String>> midMap, TblogTreeNode parentNode){
			if(parentMid == null || parentNode == null || midMap == null) return ;
			ArrayList<String> midList = midMap.get(parentMid);
			if(midList == null || midList.size() == 0) return ;
			for(String mid: midList){
				TblogTreeNode node = new TblogTreeNode(parentMid, mid);
				parentNode.add(node);
				addToParentNode(mid, midMap, node);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: tblogpath <in> <out>");
			System.exit(2);
		}
		
	    conf.setClass("mapred.output.compression.codec", LzopCodec.class, CompressionCodec.class);
	    conf.set("mapreduce.job.reduces", "100");
//	    conf.set("mapreduce.reduce.java.opts", "-Xmx2048m");
	    conf.setBoolean("mapred.output.compress", false);
	    conf.set("mapred.output.compression.type", "BLOCK");
	    conf.set("mapred.min.split.size", "512000000");

		Job job = Job.getInstance(conf, "tblogpath");
		job.setJarByClass(TblogPathGenMR.class);
		job.setMapperClass(RootMidMapper.class);
		job.setReducerClass(TreeInfoReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
