CREATE  TABLE `kol_tmp_expo`(
  `uid` string, 
  `level` string, 
  `expo_num` bigint)
PARTITIONED BY (mt string)
STORED AS RCFILE 
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/kol_tmp_expo';



CREATE  TABLE `kol_tmp_uid_mid`(
  `uid` string, 
  `mid` string, 
  `level` int)
  PARTITIONED BY (is_transmit int)
STORED AS RCFILE 
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/kol_tmp_uid_mid';



sb.append(node.getMid()).append("\t").append(node.getParentMid()).append("\t").append(node.getChildCount())
.append("\t").append(node.getLevel());

CREATE  TABLE `tblog_path_mids_tmp`(
  `mid` string, 
  `uid` string,
  `pubtime` string,
  `father_mid` string,
  `root_mid` string,
  `children_cnt` int, 
  `layer` int,
  `trans_cnt` bigint,
  `contribution` double,
  `user_level` string
  )
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'   
STORED AS TEXTFILE 
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/tblog_path_mids_tmp';









CREATE  TABLE `tblog_path_mids_info`(
  `mid` string, 
  `uid` string,
  `pubtime` string,
  `father_mid` string,
  `root_mid` string,
  `children_cnt` int, 
  `layer` int,
  `trans_cnt` bigint,
  `contribution` double,
  `user_level` string,
  `score` double
  )
PARTITIONED BY (dt string)
STORED AS RCFILE 
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/tblog_path_mids_info';

LOAD DATA INPATH "/user/wb_wls_data/tmp/tblog_test_20150904/part*" INTO TABLE tblog_path_mids_info;



grant select on table kol_influence_result to user yonghong1;
grant select on table kol_influence_result to user qinling;
grant select on table kol_influence_result to user zhaonan5;


grant select on table tblog_path_mids_info to user yonghong1;
grant select on table tblog_path_mids_info to user qinling;
grant select on table tblog_path_mids_info to user zhaonan5;

