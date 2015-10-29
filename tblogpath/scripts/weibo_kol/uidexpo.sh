
FirstDay Of Last Month:date -d "-1 month -$(($(date +%d)-1)) days"
Lastday Of Last Month:date -d "-$(date +%d) days"



last_month_start=`date -d "-1 month -$(($(date +%d)-1)) days" +%Y%m%d`
last_month_end=`date -d "-$(date +%d) days" +%Y%m%d`

select x.uid ,sum(expo_cnt) as expo_cnt from 
(select mid, appid , sum(expo_cnt ) as expo_cnt
from mds_tblog_expo_day where dt>=$last_month_start and dt<=$last_month_end 
and interface_id <>'253' group by mid, appid ) x 
join 
(select uid ,mid from influence_mid ) y 
on x.mid = y.mid 
join 
(select appid from ods_dim_appkey where dt=$last_month_end and permission_mast_code='1') z 
on x.appid = z.appid  group by uid ;

 
 
select t1.uid , sum(expo_cnt ) from 
(select s1.mid,s1.rootmid,s2.uid,s2.level from mds_bhv_pubblog s1 join influence_mid s2 on s2.mid=s1.rootmid where  s1.dt>=$last_month_start and s1.dt<=$last_month_end and is_transmit=1 ) t1
join 
(select /*+ mapjoin(s2)*/ mid, s1.appid, sum(expo_cnt ) as expo_cnt from mds_tblog_expo_day s1 join (select appid from ods_dim_appkey where dt=$last_month_end and permission_mast_code='1') s2
on s1.appid=s2.appid
where s1.dt>=$last_month_start and s1.dt<=$last_month_end and interface_id<>'253' group by mid  ) t2
on t1.mid = t2.mid group by t1.uid  ;
 
 
 
 
 
 
 
last_month_start=`date -d "-1 month -$(($(date +%d)-1)) days" +%Y%m%d`
last_month_end=`date -d "-$(date +%d) days" +%Y%m%d`
 
 
 
  自身博文曝光
select y.uid ,sum(expo_cnt) as expo_cnt from 
(select mid, appid , sum(expo_cnt ) as expo_cnt
from mds_tblog_expo_day where dt>=20150601 and dt<=20150630 
and interface_id <>'253' group by mid, appid ) x 
join 
(select uid ,mid from mds_bhv_pubblog where dt>=20150601 and dt<=20150630  ) y 
on x.mid = y.mid 
join 
(select appid from ods_dim_appkey where dt=20150630 and permission_mast_code='1') z 
on x.appid = z.appid  group by uid ;
 
 
转发博文爆光
 select t1.uid , sum(expo_cnt ) from 
(select rootuid as uid, mid from mds_bhv_pubblog where  dt>=20150601 and dt<=20150630 and is_transmit=1 ) t1
join 
(select /*+ mapjoin(s2)*/ mid, s1.appid, sum(expo_cnt ) as expo_cnt from mds_tblog_expo_day s1 join (select appid from ods_dim_appkey where dt=20150630 and permission_mast_code='1') s2
on s1.appid=s2.appid
where s1.dt>=20150601 and s1.dt<=20150630 and interface_id<>'253' group by mid  ) t2
on t1.mid = t2.mid group by t1.uid  ;
 
 
 
 
 
 
select t1.uid , sum(expo_cnt ) from 
(select uid, mid from mds_bhv_pubblog where  dt>=$last_month_start and dt<=$last_month_end ) t1
join
(select uid, level from mds_uquality_user_class where dt =$last_month_end and level='1' or level='2'  )
join 
(select mid, s1.appid, sum(expo_cnt ) as expo_cnt from mds_tblog_expo_day s1 join (select appid from ods_dim_appkey where dt=$last_month_end and permission_mast_code='1') s2
on s1.appid=s2.appid
where s1.dt>=$last_month_start and s1.dt<=$last_month_end and interface_id<>'253' group by mid  ) t2
on t1.mid = t2.mid group by t1.uid  ;
 
 
 
 
 
 
 
 
 
 
 
 select t1.uid , sum(expo_cnt ) from 
( select uid ,mid from mds_bhv_pubblog where dt>=$last_month_start and dt<=$last_month_end 
  union
  select rootuid as uid, mid from mds_bhv_pubblog where  dt>=$last_month_start and dt<=$last_month_end and is_transmit=1 ) t1
join 
(select /*+ mapjoin(s2)*/ mid, s1.appid, sum(expo_cnt ) as expo_cnt from mds_tblog_expo_day s1 join (select appid from ods_dim_appkey where dt=$last_month_end and permission_mast_code='1') s2
on s1.appid=s2.appid
where s1.dt>=$last_month_start and s1.dt<=$last_month_end and interface_id<>'253' group by mid  ) t2
on t1.mid = t2.mid group by t1.uid  ;
 
 
 
 
 
 select uid ,mid from mds_bhv_pubblog where dt>=$last_month_start and dt<=$last_month_end 
 union all
 select rootuid as uid, mid from mds_bhv_pubblog where  dt>=20150908 and dt<=20150908 and is_transmit=1
 
 
 
 
 
 
 
 
 
 
 egs of mapreduce
 mds_wls_device_diaodu.sh 
 