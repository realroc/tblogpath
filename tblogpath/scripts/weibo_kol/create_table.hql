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
  `level` int,
  `is_transmit` int)
  PARTITIONED BY (is_direct int)
STORED AS RCFILE 
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/kol_tmp_uid_mid';



CREATE  TABLE `tblog_path_mids_tmp`(
  `mid` string, 
  `uid` string,
  `pubtime` string,
  `father_mid` string,
  `root_mid` string,
  `children_cnt` string, 
  `layer` string,
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
  `nick` string,
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


CREATE  TABLE `tblog_path_mids_traned`(
  `mid` string, 
  `traned_cnt_total_day1` bigint,
  `traned_cnt_total_day2` bigint,
  `traned_cnt_total_day3` bigint,
  `traned_cnt_total_day4` bigint, 
  `traned_cnt_total_day5` bigint,
  `traned_cnt_total_day6` bigint,
  `traned_cnt_total_day7` bigint
  )
PARTITIONED BY (dt string)
STORED AS RCFILE 
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/tblog_path_mids_traned';


CREATE  TABLE `kol_influence_result`(
  `uid` string, 
  `level` string, 
  `nick` string, 
  `expo_num` double, 
  `hudong_num` double, 
  `filtered_fans_num` bigint, 
  `filtered_fans_rate` double, 
  `expo_count` double, 
  `hudong_count` double, 
  `fans_count` double, 
  `lastmonth_expo` bigint, 
  `lastmonth_original_num` bigint, 
  `lastmonth_traned_num` bigint, 
  `score` double)
PARTITIONED BY (mt string)  
STORED AS RCFILE
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/kol_influence_result' ;


--grant select on table kol_influence_result to user yonghong1;
grant select on table kol_influence_result to user qinling;
grant select on table kol_influence_result to user zhaonan5;
grant select on table kol_influence_result to user chenpeng6; 


--grant select on table tblog_path_mids_info to user yonghong1;
grant select on table tblog_path_mids_info to user qinling;
grant select on table tblog_path_mids_info to user zhaonan5;

