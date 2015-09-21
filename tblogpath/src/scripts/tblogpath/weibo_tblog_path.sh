
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