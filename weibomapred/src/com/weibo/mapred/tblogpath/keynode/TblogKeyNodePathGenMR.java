package com.weibo.mapred.tblogpath.keynode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.tree.TreeNode;

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
public class TblogKeyNodePathGenMR {

	public static class RootMidMapper extends Mapper<Object, Text, Text, Text> {
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String[] mids = value.toString().split("\001");
			context.write(new Text(mids[0]), value);
		}
	}

	public static class TreeInfoReducer extends Reducer<Text, Text, Text, Text> {
		
		HashMap<String, ArrayList<TblogTreeNode>> midNodesMap = new HashMap<String, ArrayList<TblogTreeNode>>();
		
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			
			Double keyNodeRate = Double.parseDouble(context.getConfiguration().get("keyNodeRate"));
			TblogTreeNode rootNode = null;
			
			for(Text midPair:values){
				if(midPair == null) continue ;
				String[] mids = midPair.toString().split("\001");
				if(mids == null || mids.length != 9) continue ;				
				TblogTreeNode tnode = new TblogTreeNode(mids[0], mids[1], mids[2], mids[3], mids[4], mids[5], mids[6],mids[7],mids[8]);
				if("0".equals(tnode.getParent_mid())) rootNode = tnode;
				if(midNodesMap.containsKey(mids[1])){
					midNodesMap.get(mids[1]).add(tnode);
				} else{
					ArrayList<TblogTreeNode> al = new ArrayList<TblogTreeNode>();
					al.add(tnode);
					midNodesMap.put(mids[1], al);
				}
			}
			
			String rootmid = key.toString();
			addToParentNode(rootmid, midNodesMap, rootNode);
			Enumeration<TblogTreeNode> nodeEnum = rootNode.breadthFirstEnumeration();
			//保存已经输出node,避免重复输出
			ArrayList<String> writenMidList = new ArrayList<String>();
			while(nodeEnum.hasMoreElements()){
				TblogTreeNode node = nodeEnum.nextElement();
				if(Double.parseDouble(node.getContribute()) >= keyNodeRate && ("1".equals(node.getUser_level()) || "2".equals(node.getUser_level()))){
					for(TreeNode tnode : node.getPath()){
						TblogTreeNode tn = (TblogTreeNode) tnode;
						if(!writenMidList.contains(tn.getMid())){
							StringBuffer sb = new StringBuffer();
							sb.append(tn.getMid()).append("\t").append(tn.getUid()).append("\t").append(tn.getTime()).append("\t");
							sb.append(tn.getParent_mid()).append("\t").append(tn.getRoot_mid()).append("\t").append(tn.getChildCount()).append("\t");
							sb.append(tn.getLevel()).append("\t").append(tn.getTrand_cnt()).append("\t").append(tn.getContribute()).append("\t").append(tn.getUser_level());
							context.write(null, new Text(sb.toString()));
							writenMidList.add(tn.getMid());
						}
					}
				}
			}
		}

		/**
		 * Build Multi-Tree
		 * @param parentMid
		 * @param midNodesMap key:mid value:nodes
		 * @param parentNode
		 */
		public void addToParentNode(String parentMid, HashMap<String, ArrayList<TblogTreeNode>> midNodesMap, TblogTreeNode parentNode){
			if(parentMid == null || parentNode == null || midNodesMap == null) return ;
			ArrayList<TblogTreeNode> nodeList = midNodesMap.get(parentMid);
			if(nodeList == null || nodeList.size() == 0) return ;
			for(TblogTreeNode node: nodeList){
				parentNode.add(node);
				addToParentNode(node.getMid(), midNodesMap, node);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 3) {
			System.err.println("Usage: tblogpath <in> <out> <keynoderate>");
			System.exit(2);
		}
		
		conf.set("keyNodeRate", otherArgs[2]);
		
	    conf.setClass("mapred.output.compression.codec", LzopCodec.class, CompressionCodec.class);
	    conf.set("mapreduce.job.reduces", "100");
	    conf.setBoolean("mapred.output.compress", false);
	    conf.set("mapred.output.compression.type", "BLOCK");
	    conf.set("mapred.min.split.size", "512000000");
	    
		Job job = Job.getInstance(conf, "tblogpath");
		job.setJarByClass(TblogKeyNodePathGenMR.class);
		job.setMapperClass(RootMidMapper.class);
		job.setReducerClass(TreeInfoReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
