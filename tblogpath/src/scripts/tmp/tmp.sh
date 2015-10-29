
--Search_Tmp_Cheat_Data

CREATE  TABLE `search_tmp_cheat_user`(
  `uid` string
  )
ROW FORMAT DELIMITED 
  FIELDS TERMINATED BY '\t' 
STORED AS TEXTFILE
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/search_tmp_cheat_user' ;


LOAD DATA LOCAL INPATH '/tmp/cheat.qingtao.data' OVERWRITE INTO TABLE search_tmp_cheat_user;

grant select on table search_tmp_cheat_user to user zhaonan5 ;