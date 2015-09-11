
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
insert overwrite table tblog_path_parentmid_mid partition(dt='$before_dt_7days')
select /*+ mapjoin(t1)*/ t2.rootmid, t2.parentmid, t2.mid from 
(select a.mid from
    (select mid from mds_bhv_pubblog where dt=$before_dt_7days and is_transmit=0) a
    join
    (select mid, sum(traned_cnt_total) as traned_cnt_total from mds_tblog_bhv_day where dt>=$before_dt_7days and dt<$dt group by mid) b
    on a.mid=b.mid where traned_cnt_total >= 100 
) t1 
join 
(select rootmid, parentmid, mid 
    from mds_bhv_pubblog  where dt>=$before_dt_7days and dt<$dt and is_transmit=1
) t2
on t1.mid = t2.rootmid;
"

echo "$exec_hql"
hive -e "$exec_hql" 