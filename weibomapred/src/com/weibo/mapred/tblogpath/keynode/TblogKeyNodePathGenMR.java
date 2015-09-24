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
		
		HashMap<String, ArrayList<NodeInfo>> midMap = new HashMap<String, ArrayList<NodeInfo>>();
		
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			
			Double keyNodeRate = Double.parseDouble(context.getConfiguration().get("keyNodeRate"));
			
			NodeInfo rootNodeInf = null;
			for(Text midPair:values){
				if(midPair == null) continue ;
				String[] mids = midPair.toString().split("\001");
				if(mids == null || mids.length != 9) continue ;				
				NodeInfo nodeInfo = new NodeInfo(mids[0], mids[1], mids[2], mids[3], mids[4], mids[5], mids[6],mids[7],mids[8]);
				if("0".equals(nodeInfo.getParentmid())) rootNodeInf = nodeInfo;
				if(midMap.containsKey(mids[1])){
					midMap.get(mids[1]).add(nodeInfo);
				} else{
					ArrayList<NodeInfo> al = new ArrayList<NodeInfo>();
					al.add(nodeInfo);
					midMap.put(mids[1], al);
				}
			}
			
			String rootmid = key.toString();
			TblogTreeNode rootNode = new TblogTreeNode(rootmid, rootNodeInf);
			addToParentNode(rootmid, midMap, rootNode);
			Enumeration<TblogTreeNode> nodeEnum = rootNode.breadthFirstEnumeration();
			//保存已经输出node,避免重复输出
			ArrayList<String> writenMidList = new ArrayList<String>();
			while(nodeEnum.hasMoreElements()){
				TblogTreeNode node = nodeEnum.nextElement();
				NodeInfo n = node.getNodeInf();
				if(Double.parseDouble(n.getContribute()) >= keyNodeRate && ("1".equals(n.getLevel()) || "2".equals(n.getLevel()) )){
					for(TreeNode tnode : node.getPath()){
						TblogTreeNode tn = (TblogTreeNode) tnode;
						if(!writenMidList.contains(tn.getNodeInf().getMid())){
							StringBuffer sb = new StringBuffer();
							NodeInfo tempNode = tn.getNodeInf();
							sb.append(tempNode.getMid()).append("\t").append(tempNode.getUid()).append("\t").append(tempNode.getTime()).append("\t");
							sb.append(tempNode.getParentmid()).append("\t").append(tempNode.getRootmid()).append("\t").append(tn.getChildCount()).append("\t");
							sb.append(tn.getLevel()).append("\t").append(tempNode.getTrand_cnt()).append("\t").append(tempNode.getContribute()).append("\t").append(tempNode.getLevel());
							context.write(null, new Text(sb.toString()));
							writenMidList.add(tn.getNodeInf().getMid());
						}
					}
				}
			}
		}

		/**
		 * build path tree
		 * @param parentMid
		 * @param midMap
		 * @param parentNode
		 */
		public void addToParentNode(String parentMid, HashMap<String, ArrayList<NodeInfo>> midMap, TblogTreeNode parentNode){
			if(parentMid == null || parentNode == null || midMap == null) return ;
			ArrayList<NodeInfo> midList = midMap.get(parentMid);
			if(midList == null || midList.size() == 0) return ;
			for(NodeInfo mid: midList){
				TblogTreeNode node = new TblogTreeNode(parentMid, mid);
				parentNode.add(node);
				addToParentNode(mid.getMid(), midMap, node);
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
