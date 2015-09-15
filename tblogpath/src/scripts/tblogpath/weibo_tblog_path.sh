
if test "$1" == "" ; then
   dt=`date +%Y%m%d`
else
   dt=$1
fi

before_dt_7days=`date -d"$dt -7 day" +"%Y%m%d"`

echo $dt
echo $before_dt_7days



##ɸѡ������ǰ����ԭ������(������ת�������ڵ���100��)��������ת������
exec_hql="
drop table if exists tblog_path_parentmid_mid_tmp;
create table tblog_path_parentmid_mid_tmp as 
select /*+ mapjoin(t1)*/ t2.rootmid, t2.parentmid, t2.mid, time, uid, t1.traned_cnt_total as rootmid_trand_cnt , t2.traned_cnt_total as mid_trand_cnt from 
(select a.mid, traned_cnt_total from
    (select mid from mds_bhv_pubblog where dt=$before_dt_7days and is_transmit=0) a
    join
    (select mid, sum(traned_cnt_total) as traned_cnt_total from mds_tblog_bhv_day where dt>=$before_dt_7days and dt<$dt group by mid) b
    on a.mid=b.mid where traned_cnt_total >= 100 
) t1 
join 
(select rootmid, parentmid, x.mid, time, uid,  case when traned_cnt_total is null then 0 else traned_cnt_total end as traned_cnt_total from 
    (select rootmid, parentmid, mid, time, uid from mds_bhv_pubblog where dt>=$before_dt_7days and dt<=dt<$dt and is_transmit=1) x 
    left join 
    (select mid, sum(traned_cnt_total) as traned_cnt_total from mds_tblog_bhv_day where dt>=$before_dt_7days and dt<$dt group by mid) y 
    on x.mid = y.mid 
) t2
on t1.mid = t2.rootmid;
"

#echo "$exec_hql"
#hive -e "$exec_hql" 



##����ԭ���������·������
inputFolder="/user/wb_wls_data/warehouse/tblog_path_parentmid_mid_tmp"
outputFolder="/user/wb_wls_data/tmp/tblog_path_mids_tmp"
 
hadoop fs -test -d $outputFolder
if [ $? -eq 0 ]; then
    hadoop fs -rmr $outputFolder 
fi

#hadoop jar /home/wb_wls_data/weibo_tblog_path/tblogpath.jar com.weibo.mapred.tblogpath.TblogPathGenMR $inputFolder $outputFolder

exec_hql="
LOAD DATA INPATH '$outputFolder/part*' INTO TABLE tblog_path_mids_tmp;
"
echo "$exec_hql"
#hive -e "$exec_hql"


##����ת��������

exec_hql="
set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table tblog_path_mids_info partition(dt=$before_dt_7days)
select a.mid, a.uid, a.time, a.parentmid, a.rootmid, b.children_cnt, b.layer, a.mid_trand_cnt/a.rootmid_trand_cnt as ctrb_rate from tblog_path_parentmid_mid_tmp a 
join 
tblog_path_mids_tmp b on a.mid = b.mid 
"

echo "$exec_hql"
hive -e "$exec_hql"