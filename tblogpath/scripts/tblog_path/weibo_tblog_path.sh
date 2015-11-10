if test "$1" == "" ; then
   dt=`date +%Y%m%d`
else
   dt=$1
fi

yestoday=`date -d"$dt -1 day" +"%Y%m%d"`
before_dt_7days=`date -d"$dt -7 day" +"%Y%m%d"`
lastmonth=`date -d"$dt -1 month" +"%Y%m"`
last2month=`date -d"$dt -2 month" +"%Y%m"`

hadoop fs -test -e /user/wb_wls_data/warehouse/kol_influence_result/mt=$lastmonth
if [ $? -ne 0 ]; then
    kol_month=$last2month
else
    kol_month=$lastmonth
fi

echo $dt
echo $before_dt_7days
echo $kol_month



##筛选第七天前新增原创博文(七天内转发数大于等于100次)及其所有转发博文
exec_hql="
drop table if exists tblog_path_parentmid_mid_tmp;
create table tblog_path_parentmid_mid_tmp as 
select /*+ mapjoin(t1)*/ t2.rootmid, t2.parentmid, t2.mid, time, t2.uid, t1.traned_cnt_total as rootmid_traned_cnt_total, traned_cnt, traned_cnt/traned_cnt_total as contribute, level from 
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
on t1.mid = t2.rootmid
left join
(select uid, level from mds_uquality_user_class where dt='$yestoday' and (level='1' or level='2')) t3
on t2.uid = t3.uid;
"

echo "$exec_hql"
hive -e "$exec_hql" 



##生成原创博文关键节点路径数据
inputFolder="/user/wb_wls_data/warehouse/tblog_path_parentmid_mid_tmp"
outputFolder="/user/wb_wls_data/tmp/tblog_path_mids_tmp"
keyNodeRate=0.1
 
hadoop fs -test -d $outputFolder
if [ $? -eq 0 ]; then
    hadoop fs -rmr $outputFolder 
fi

hadoop jar /home/wb_wls_data/weibo_tblog_path/tblogpath.jar com.weibo.mapred.tblogpath.keynode.TblogKeyNodePathGenMR $inputFolder $outputFolder $keyNodeRate

exec_hql="
LOAD DATA INPATH '$outputFolder/part*' OVERWRITE INTO TABLE tblog_path_mids_tmp;
"
echo "$exec_hql"
hive -e "$exec_hql"


##查询kol信息 
exec_hql="
insert overwrite table tblog_path_mids_info partition(dt=$before_dt_7days)
select a.mid, a.uid, a.pubtime, a.father_mid, a.root_mid, a.children_cnt, a.layer, a.trans_cnt, a.contribution, a.user_level, b.score 
from tblog_path_mids_tmp a 
left join 
(select uid, score from kol_influence_result where mt='$kol_month') b on a.uid = b.uid
"
echo "$exec_hql"
hive -e "$exec_hql"


##原创博文一周内每天累计传播次数
exec_hql="
insert overwrite table tblog_path_mids_traned partition(dt=$before_dt_7days)
select mid,
        sum(if(days < 1,traned_cnt_total,0)),
        sum(if(days < 2,traned_cnt_total,0)),
        sum(if(days < 3,traned_cnt_total,0)),
        sum(if(days < 4,traned_cnt_total,0)),
        sum(if(days < 5,traned_cnt_total,0)),
        sum(if(days < 6,traned_cnt_total,0)),
        sum(if(days < 7,traned_cnt_total,0))
        from
(
select /*+ mapjoin(a)*/ a.mid, a.dt, (unix_timestamp(b.dt,'yyyyMMdd') - unix_timestamp('$before_dt_7days','yyyyMMdd'))/(60*60*24) as days, traned_cnt_total from
(select mid, dt from tblog_path_mids_info where dt='$before_dt_7days' and layer=0) a
join
(select mid, dt, traned_cnt_total from mds_tblog_bhv_day where dt>='$before_dt_7days' and dt<'$dt') b 
on a.mid = b.mid 
)c group by mid, dt;
"
echo "$exec_hql"
hive -e "$exec_hql"
