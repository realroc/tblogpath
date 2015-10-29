

exec_hql="
insert overwrite table true_active_user_tmp partition(type='ulevel')
select uid from mds_uquality_user_class where level=1 and dt='20151015' and length(uid)<=10 group by uid;

insert overwrite table true_active_user_tmp partition(type='push')
select uid from mds_wls_encode_bhv where dt>='20151009' and dt<='20151015'  and action in (7,26) and previous_uicode in (10000095,10000192,10000043,10000210,10000261,10000203,10000208,10000221,10000218,10000174,10000206,10000207,10000323,10000324,10000325,10000262,10000338) and uid rlike '^[0-9]+$' and  length(uid)<=10 group by uid ;

insert overwrite table true_active_user_tmp partition(type='paybind')
select uid from mds_business_taobao_bind_info where dt='20151015' and status=0 and length(uid)<=10 group by uid ;

insert overwrite table true_active_user_tmp partition(type='clientstart')
select uid from mds_wls_encode_bhv where dt>='20151009' and dt<='20151015' and action=496 and extend like '%flag:1%' and from_val like '10%' and (version=0 or version is null) and length(uid)<=10 group by uid ;

insert overwrite table true_active_user_tmp partition(type='pub')
select uid from mds_bhv_tblog_day where dt>='20151009' and dt<='20151015' and (pub_cnt>=1 or tran_cnt>=1 or cmt_cnt>=1 or new_atten_cnt>=1) and length(uid)<=10 group by uid ;
        
insert overwrite table true_active_user_tmp partition(type='zan')
select uid from mds_bhv_like where dt>='20151009' and dt<='20151015' and length(uid)<=10 group by uid ;

insert overwrite table true_active_user_tmp partition(type='refresh')
select uid from mds_tblog_expo_user_day where interface_id in (1,5,8,2,3,6,201,202,203,207,800,900,700,50) and dt>='20151009' and dt<='20151015' and length(uid)<=10 group by uid ;

insert overwrite table true_active_user_tmp partition(type='search')
select uid from search_temp_detail where dt>='20151009' and dt<='20151015' and length(uid)<=10 group by uid ;
"
echo $exec_hql
hive -e "$exec_hql"



exec_hql="
set mapred.reduce.tasks=100;
insert overwrite table true_active_user_tmp partition(type='result')
select a.uid from
(select uid from true_active_user_tmp where type='ulevel') a
join
(select uid from true_active_user_tmp where type in ('push','paybind','clientstart')) b
on a.uid = b.uid
join
(select uid from true_active_user_tmp where type in ('pub','zan','refresh')) c
on a.uid = c.uid 
join
(select uid from true_active_user_tmp where type in ('search')) d
on a.uid = d.uid
group by a.uid
"
echo $exec_hql
hive -e "$exec_hql"


#exec_hql="
explain
select a.uid from 
(
        select uid from mds_uquality_user_class where level=1 and dt='20151015' and length(uid)<=10 group by uid
) a
join
(
        select uid from mds_wls_encode_bhv where dt>='20151009' and dt<='20151015'  and action in (7,26) and previous_uicode in (10000095,10000192,10000043,10000210,10000261,10000203,10000208,10000221,10000218,10000174,10000206,10000207,10000323,10000324,10000325,10000262,10000338) and uid rlike '^[0-9]+$' and  length(uid)<=10 group by uid
        UNION ALL
        select uid from mds_business_taobao_bind_info where dt='20151009' and status=0 and length(uid)<=10 group by uid
        UNION ALL
        select uid from mds_wls_encode_bhv where dt>='20151009' and dt<='20151015' and action=496 and extend like '%flag:1%' and from_val like '10%' and (version=0 or version is null) and length(uid)<=10 group by uid
) b
on a.uid=b.uid
join
(
        select uid from mds_bhv_tblog_day where dt>='20151009' and dt<='20151015' and (pub_cnt>=1 or tran_cnt>=1 or cmt_cnt>=1 or new_atten_cnt>=1) and length(uid)<=10 group by uid
        UNION ALL
        select uid from mds_bhv_like where dt>='20151009' and dt<='20151015' and length(uid)<=10 group by uid
        UNION ALL
        select uid from mds_tblog_expo_user_day where interface_id in (1,5,8,2,3,6,201,202,203,207,800,900,700,50) and dt>='20151009' and dt<='20151015' and length(uid)<=10 group by uid ;
        UNION ALL
        select uid from search_temp_detail where dt>='20151009' and dt<='20151015' and length(uid)<=10 group by uid
) c
on a.uid = c.uid group by a.uid
"  
#echo $exec_hql
#hive -e "$exec_hql" 


CREATE  TABLE `true_active_user_tmp`(
  `uid` string)
PARTITIONED BY ( 
  `type` string)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' 
STORED AS TEXTFILE 
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/true_active_user_tmp'


create table true_active_user_result as
select uid from true_active_user_tmp where type = 'result' ;

grant select on table true_active_user_result to user renhan;
grant select on table true_active_user_result to user jinqiang;
grant select on table true_active_user_result to user chengang6;
grant select on table true_active_user_result to user zhaonan5;
