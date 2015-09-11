package com.weibo.mapred.tblogpath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;;;

/**
 * 博文传播路径生成
 * @author zengpeng
 * 2015年9月11日 下午5:23:29
 */
public class TblogPathGenMR {

	public static class RootMidMapper extends Mapper<Object, Text, Text, Text> {

		private Text rootMid = new Text();
		private Text midPair = new Text();

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String[] mids = value.toString().split("\001");
			rootMid.set(mids[0]);
			midPair.set(mids[1] + "\t" + mids[2]);
			context.write(rootMid, midPair);
		}
	}

	public static class TreeInfoReducer extends Reducer<Text, Text, Text, Text> {
		
		HashMap<String, ArrayList<String>> midMap = new HashMap<String, ArrayList<String>>();
		
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			
			for(Text midPair:values){
				if(midPair == null) continue ;
				String[] mids = midPair.toString().split("\t");
//context.write(new Text("test" + mids[0] + "|" + mids[1] + "|" + mids.length), midPair);				
				if(mids == null || mids.length != 2) continue ;
				if(midMap.containsKey(mids[0])){
					midMap.get(mids[0]).add(mids[1]);
				} else{
					ArrayList<String> al = new ArrayList<String>();
					al.add(mids[1]);
					midMap.put(mids[0], al);
				}
			}
			
			String rootmid = key.toString();
			TblogTreeNode rootNode = new TblogTreeNode(null, rootmid);
			addToParentNode(rootmid, midMap, rootNode);
			
			Enumeration<TblogTreeNode> nodeEnum = rootNode.breadthFirstEnumeration();
			
			while(nodeEnum.hasMoreElements()){
				TblogTreeNode node = nodeEnum.nextElement();
				StringBuffer sb = new StringBuffer();
				sb.append(node.getMid()).append("\t").append(node.getParentMid()).append("\t").append(node.getChildCount())
				.append("\t").append(node.getLevel());
//				context.write(key, new Text(sb.toString()));
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
		Job job = Job.getInstance(conf, "tblogpath");
		job.setJarByClass(TblogPathGenMR.class);
		job.setMapperClass(RootMidMapper.class);
		job.setCombinerClass(TreeInfoReducer.class);
		job.setReducerClass(TreeInfoReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
