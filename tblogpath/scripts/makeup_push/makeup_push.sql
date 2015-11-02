

CREATE  TABLE `huyuan_push_type`(
  `uid` string)
PARTITIONED BY ( 
  `type` string)
STORED AS TEXTFILe
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/huyuan_push_type'


insert overwrite  table huyuan_push_type partition(type='A1') 


（1）基础表-用户质量1~2级 +可push+过去7天客户端登录天数
create table huyuan_push0 as
select n1.uid,lday from
 (select m1.uid from
   (select uid from mds_uquality_user_class where dt='20151023' and level>=1 and level<=2 group by uid)m1
  join
   (select uid from ods_bas_pushable_uidlist_valid where dt='20151024' and valid_flag=1)m2
  on m1.uid=m2.uid)n1
left outer join
 (select uid,count(distinct dt) as lday from ods_wls_login where dt>='20151018' and dt<='20151024' and wfrom like '10%' and (version=0 or version is null) group by uid)n2
on n1.uid=n2.uid


（2）A1-近一周美妆头部用户粉丝互动(全站)
insert overwrite  table huyuan_push_type partition(type='A1') 
select m.fans_uid as uid from
  (select fans_uid,b.uid from
   (select uid from huyuan_push0 where lday is null or (lday>=1 and lday<=5) group by uid)a
  join 
   (select fans_uid,x.uid from
     (select uid from hue_xiongtao1_user_category_1014 where category_name='美妆' group by uid)x
    join 
     (select fans_uid,uid from mds_user_fanslist where dt='20151024')y
    on x.uid=y.uid)b
   on a.uid=b.fans_uid)m
 join
  (select uid,bid from 
    (select uid,rootuid as bid from mds_bhv_pubblog where dt>='20151018' and dt<='20151024' and is_transmit=1 group by uid,rootuid
     union all
     select uid,author_uid as bid from mds_bhv_cmtblog where dt>='20151018' and dt<='20151024' group by uid,author_uid
     union all
   select uid,object_owner as bid from mds_bhv_like where dt>='20151018' and dt<='20151024' and mode='1' and status='1' group by uid,object_owner)w
   group by uid,bid)n
on m.fans_uid=n.uid and m.uid=n.bid
group by fans_uid


（3）A2-上一周美妆头部用户粉丝互动(全站)
insert overwrite  table huyuan_push_type partition(type='A2') 
select m.fans_uid as uid  from
  (select fans_uid,b.uid from
   (select uid from huyuan_push0 where lday is null or (lday>=1 and lday<=5) group by uid)a
  join 
   (select fans_uid,x.uid from
     (select uid from hue_xiongtao1_user_category_1014 where category_name='美妆' group by uid)x
    join 
     (select fans_uid,uid from mds_user_fanslist where dt='20151024')y
    on x.uid=y.uid)b
   on a.uid=b.fans_uid)m
 join
  (select uid,bid from 
    (select uid,rootuid as bid from mds_bhv_pubblog where dt>='20151011' and dt<='20151017' and is_transmit=1 group by uid,rootuid
     union all
     select uid,author_uid as bid from mds_bhv_cmtblog where dt>='20151011' and dt<='20151017'  group by uid,author_uid
     union all
   select uid,object_owner as bid from mds_bhv_like where dt>='20151011' and dt<='20151017' and mode='1' and status='1' group by uid,object_owner)w
   group by uid,bid)n
on m.fans_uid=n.uid and m.uid=n.bid
group by fans_uid


（4）A6-近一周访问美妆热门微博榜
insert overwrite  table huyuan_push_type partition(type='A6') 
select a.uid from
 (select uid from huyuan_push0 where lday is null or (lday>=1 and lday<=5) group by uid)a
join 
 (select uid from mds_wls_encode_bhv where  dt>='20151018' and dt<='20151024' and action in ('26','27') and main_id='102803_ctg1_1588_-_ctg1_1588' group by uid)b
on a.uid=b.uid
group by a.uid


（5）A7-近一周访问美妆热门话题榜
insert overwrite  table huyuan_push_type partition(type='A7') 
select a.uid  from
 (select uid from huyuan_push0 where lday is null or (lday>=1 and lday<=5) group by uid)a
join 
 (select uid  from mds_wls_encode_bhv where dt>='20151018' and dt<='20151024'and action in ('26','27') 
  and main_id in ('100803_ctg1_114_-_page_topics_ctg1__114','100803_ctg1_114002_-_page_ctg_hot_list__114002','100803_ctg1_114002_-_page_topics_all_ctg__114002',
                  '100803_ctg1_114001_-_page_ctg_hot_list__114001','100803_ctg1_114001_-_page_topics_all_ctg__114001',
                  '100803_ctg1_114004_-_page_ctg_hot_list__114004','100803_ctg1_114004_-_page_topics_all_ctg__114004',
                  '100803_ctg1_114003_-_page_ctg_hot_list__114003','100803_ctg1_114003_-_page_topics_all_ctg__114003',
                  '100803_ctg1_114_-_page_topics_all_ctg__114','100803_ctg1_114_-_page_ctg_hot_list__114') group by uid)b
on a.uid=b.uid
group by a.uid


（6）A8-近一周访问美妆话题page页
add jar /usr/local/jobclient/bin/jar/hive_function.jar;
create temporary function findid as 'UDF.FindId';
insert overwrite  table huyuan_push_type partition(type='A8') 
select a.uid from
 (select uid from huyuan_push0 where lday is null or (lday>=1 and lday<=5) group by uid)a
join 
 (select uid  from
   (select uid,substr(main_id,7) as huati_md5  from mds_wls_encode_bhv where dt>='20151018' and dt<='20151024' and action in ('6','27') and main_id like '100808%' group by uid,substr(main_id,7) )x
  join 
   (select substr(object_id,12,32) as huati_md5 from ods_obj_base_info where dt='20151024' and object_type='topic' and split(findid(extend,'category'),'\\|')[0]='化妆造型' group by substr(object_id,12,32) )y
  on x.huati_md5=y.huati_md5
  group by uid)b
on a.uid=b.uid
group by a.uid


（7）A9-近一周找人-美妆分类下访问
insert overwrite  table huyuan_push_type partition(type='A9') 
select a.uid from
 (select uid from huyuan_push0 where lday is null or (lday>=1 and lday<=5) group by uid)a
join 
 (select uid from mds_wls_encode_bhv where dt>='20151018' and dt<='20151024' and action in ('26','27') and main_id='1087030002_2150_2014'  group by uid)b
on a.uid=b.uid
group by a.uid


（8）A10-近一周美妆头部用户粉丝访问profile
insert overwrite  table huyuan_push_type partition(type='A10') 
select m.fans_uid as uid  from
  (select fans_uid,b.uid from
   (select uid from huyuan_push0 where lday is null or (lday>=1 and lday<=5) group by uid)a
  join 
   (select fans_uid,x.uid from
     (select uid from hue_xiongtao1_user_category_1014 where category_name='美妆' group by uid)x
    join 
     (select fans_uid,uid from mds_user_fanslist where dt='20151024')y
    on x.uid=y.uid)b
   on a.uid=b.fans_uid)m
 join
  (select uid,substr(split(main_id,'_-_')[0],7) as bid from mds_wls_encode_bhv where dt>='20151018' and dt<='20151024'  and action=26 and uicode='10000198' and from_val like '10%' and (version=0 or version is null) group by uid,substr(split(main_id,'_-_')[0],7))n
on m.fans_uid=n.uid and m.uid=n.bid
group by fans_uid


（9）A11-上一周美妆头部用户粉丝访问profile
insert overwrite  table huyuan_push_type partition(type='A11') 
select m.fans_uid as uid from
  (select fans_uid,b.uid from
   (select uid from huyuan_push0 where lday is null or (lday>=1 and lday<=5) group by uid)a
  join 
   (select fans_uid,x.uid from
     (select uid from hue_xiongtao1_user_category_1014 where category_name='美妆' group by uid)x
    join 
     (select fans_uid,uid from mds_user_fanslist where dt='20151024')y
    on x.uid=y.uid)b
   on a.uid=b.fans_uid)m
 join
  (select uid,substr(split(main_id,'_-_')[0],7) as bid from mds_wls_encode_bhv where dt>='20151011' and dt<='20151017'  and action=26 and uicode='10000198' and from_val like '10%' and (version=0 or version is null) group by uid,substr(split(main_id,'_-_')[0],7))n
on m.fans_uid=n.uid and m.uid=n.bid
group by fans_uid


insert overwrite  table huyuan_push_type partition(type='result')
select uid from huyuan_push_type group by uid ;


insert overwrite local directory '/tmp/huyuan/00_24'
select uid from huyuan_push_type where type = 'result' and substr(uid,-3,2)<=24

insert overwrite local directory '/tmp/huyuan/25_49'
select uid from huyuan_push_type where type = 'result' and substr(uid,-3,2)>=25 and substr(uid,-3,2)<=49

insert overwrite local directory '/tmp/huyuan/50_74'
select uid from huyuan_push_type where type = 'result' and substr(uid,-3,2)>=50 and substr(uid,-3,2)<=74

insert overwrite local directory '/tmp/huyuan/75_99'
select uid from huyuan_push_type where type = 'result' and substr(uid,-3,2)>=75





（10）将上述8个条件的uid做一个排重
create table hue_huyuan_pushmz row format DELIMITED FIELDS TERMINATED BY '\t' 
location '/user/huyuan/warehouse/tables/hue_huyuan_pushmz' 
select uid from   
    (select uid from hue_huyuan_a1 group by uid
     union all
     select uid from hue_huyuan_a2 group by uid
     union all
     select uid from hue_huyuan_a6 group by uid
     union all
     select uid from hue_huyuan_a7 group by uid
     union all
     select uid from hue_huyuan_a8 group by uid
     union all
     select uid from hue_huyuan_a9 group by uid
     union all
     select uid from hue_huyuan_a10 group by uid
     union all
     select uid from hue_huyuan_a11 group by uid)t
group by uid


（11） 根据uid倒数第23位分成3个实验组和1个对照组

实验组1
create table hue_huyuan_pushmz_syz1 row format DELIMITED FIELDS TERMINATED BY '\t' 
location '/user/huyuan/warehouse/tables/hue_huyuan_pushmz_syz1' as
select uid from hue_huyuan_pushmz where substr(uid,-3,2)<=24

实验组2
create table hue_huyuan_pushmz_syz2 row format DELIMITED FIELDS TERMINATED BY '\t' 
location '/user/huyuan/warehouse/tables/hue_huyuan_pushmz_syz2' as
select uid from hue_huyuan_pushmz where substr(uid,-3,2)>=25 and substr(uid,-3,2)<=49

实验组3
create table hue_huyuan_pushmz_syz3 row format DELIMITED FIELDS TERMINATED BY '\t' 
location '/user/huyuan/warehouse/tables/hue_huyuan_pushmz_syz3' as
select uid from hue_huyuan_pushmz where substr(uid,-3,2)>=50 and and substr(uid,-3,2)<=74

对照组
create table hue_huyuan_pushmz_dzz1 row format DELIMITED FIELDS TERMINATED BY '\t' 
location '/user/huyuan/warehouse/tables/hue_huyuan_pushmz_dzz1' as
select uid from hue_huyuan_pushmz where substr(uid,-3,2)>=75
