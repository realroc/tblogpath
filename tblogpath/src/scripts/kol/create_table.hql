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




CREATE  TABLE `tblog_path_parentmid_mid`(
  `rootmid` string, 
  `parentmid` string, 
  `mid` string
  )
  PARTITIONED BY (dt string)
STORED AS TEXTFILE 
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/tblog_path_parentmid_mid';




CREATE  TABLE `tblog_path_mids_tmp`(
  `rootmid` string, 
  `mid` string,
  `parentmid` string, 
  `children_cnt` string, 
  `layer` string
  )
ROW FORMAT DELIMITED FIELDS TERMINATED BY "\t"
STORED AS TEXTFILE 
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/tblog_path_mids_tmp';

LOAD DATA INPATH "/user/wb_wls_data/tmp/tblog_test_20150904/part*" INTO TABLE tblog_path_mids_tmp;





