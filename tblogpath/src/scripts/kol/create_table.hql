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