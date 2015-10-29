
if test "$1" == "" ; then
   dt=`date +%Y%m%d`
else
   dt=$1
fi

before_dt_7days=`date -d"$dt -7 day" +"%Y%m%d"`

echo $dt
echo $before_dt_7days



##筛选第七天前新增原创博文(七天内转发数大于等于100次)及其所有转发博文
exec_hql="
drop table if exists tblog_path_parentmid_mid_tmp;
create table tblog_path_parentmid_mid_tmp as 
select /*+ mapjoin(t1)*/ t2.rootmid, t2.parentmid, t2.mid, time, uid, t1.traned_cnt_total as rootmid_trand_cnt_total , t2.traned_cnt as trand_cnt from 
(select a.mid, traned_cnt_total from
    (select mid from mds_bhv_pubblog where dt=$before_dt_7days and is_transmit=0) a
    join
    (select mid, sum(traned_cnt_total) as traned_cnt_total from mds_tblog_bhv_day where dt>=$before_dt_7days and dt<$dt group by mid) b
    on a.mid=b.mid where traned_cnt_total >= 100 
) t1 
join 
(select rootmid, parentmid, x.mid, time, uid,  case when traned_cnt is null then 0 else traned_cnt end as traned_cnt from 
    (select mid as rootmid, 0 as parentmid, mid, time, uid from mds_bhv_pubblog where dt=$before_dt_7days and is_transmit=0
     union all
     select rootmid, parentmid, mid, time, uid from mds_bhv_pubblog where dt>=$before_dt_7days and dt<$dt and is_transmit=1
    ) x 
    left join 
    (select mid, sum(tranded_cnt) as traned_cnt from mds_tblog_bhv_day where dt>=$before_dt_7days and dt<$dt group by mid) y 
    on x.mid = y.mid 
) t2
on t1.mid = t2.rootmid;
"

echo "$exec_hql"
hive -e "$exec_hql" 



##生成原创博文相关路径数据
inputFolder="/user/wb_wls_data/warehouse/tblog_path_parentmid_mid_tmp"
outputFolder="/user/wb_wls_data/tmp/tblog_path_mids_tmp"
 
hadoop fs -test -d $outputFolder
if [ $? -eq 0 ]; then
    hadoop fs -rmr $outputFolder 
fi

hadoop jar /home/wb_wls_data/weibo_tblog_path/tblogpath.jar com.weibo.mapred.tblogpath.TblogPathGenMR $inputFolder $outputFolder

exec_hql="
LOAD DATA INPATH '$outputFolder/part*' OVERWRITE INTO TABLE tblog_path_mids_tmp;
"
echo "$exec_hql"
hive -e "$exec_hql"


##计算转发贡献率
exec_hql="
set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tblog_path_mids_info partition(dt=$before_dt_7days)
select a.mid, a.uid, a.time, a.parentmid, a.rootmid, b.children_cnt, b.layer, a.trand_cnt, a.trand_cnt/a.rootmid_trand_cnt_total as ctrb_rate 
from tblog_path_parentmid_mid_tmp a 
join 
tblog_path_mids_tmp b on a.mid = b.mid 
"
echo "$exec_hql"
hive -e "$exec_hql"



##用户质量过滤
exec_hql="
insert overwrite table tblog_path_mids_info partition(dt=0000)
select /*+ mapjoin(a)*/ c.mid, c.uid, c.pubtime, c.father_mid, c.root_mid, c.children_cnt, c.layer, c.trans_cnt, c.contribution from 
    (select uid, root_mid from tblog_path_mids_info where dt='$before_dt_7days' and father_mid=0) a
    join
    (select uid, level from mds_uquality_user_class where dt='$before_dt_7days' and (level='1' or level='2')) b
    on a.uid = b.uid 
    join
    (select mid, uid, pubtime, father_mid, root_mid, children_cnt, layer, trans_cnt, contribution from tblog_path_mids_info where dt='$before_dt_7days') c
    on a.root_mid = c.root_mid;
"
echo "$exec_hql"
hive -e "$exec_hql"



##查询原创博文七天内传播量















exec_hql="
select /*+ mapjoin(a)*/ a.mid, dt, (unix_timestamp('$dt','yyyy-MM-dd') - unix_timestamp('$before_dt_7days','yyyy-MM-dd'))/(60*60*24) as days, traned_cnt_total from
(select mid from tblog_path_mids_info where dt=$before_dt_7days and layer=0) a
join
(select mid, dt, traned_cnt_total from mds_tblog_bhv_day where dt>='$before_dt_7days' and dt<'$dt' and is_transmit='0' ) b 
on a.mid = b.mid ;
"
echo "$exec_hql"
hive -e "$exec_hql"









	   


select mid,
        dt,
        sum(if(days < 1,traned_cnt_total,0)),
        sum(if(days < 2,traned_cnt_total,0)),
        sum(if(days < 3,traned_cnt_total,0)),
        sum(if(days < 4,traned_cnt_total,0)),
        sum(if(days < 5,traned_cnt_total,0)),
        sum(if(days < 6,traned_cnt_total,0)),
        sum(if(days < 7,traned_cnt_total,0))
        from
(
select /*+ mapjoin(a)*/ a.mid, a.dt, (unix_timestamp(b.dt,'yyyyMMdd') - unix_timestamp('','yyyyMMdd'))/(60*60*24) as days, traned_cnt_total from
(select mid, dt from tblog_path_mids_info where dt='' and layer=0) a
join
(select mid, dt, traned_cnt_total from mds_tblog_bhv_day where dt>='' and dt<'') b
on a.mid = b.mid
)c group by mid, dt limit 200;





3896549186501015






SELECT (unix_timestamp('2010-09-11','yyyy-MM-dd') - unix_timestamp('2010-09-12','yyyy-MM-dd'))/(60*60*24) from search_tmp_cheat_user LIMIT 1;


SELECT datediff('2010-09-11', '2010-09-22') FROM search_tmp_cheat_user LIMIT 1;


##unix_timestamp() returns an int: current time in seconds since epoch
##from_unixtime(,'yyyy-MM-dd') converts to a string of the given format, e.g. '2012-12-28'
##date_sub(,180) subtracts 180 days from that string, and returns a new string in the same format.
##unix_timestamp(,'yyyy-MM-dd') converts that string back to an int

