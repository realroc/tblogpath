
if test "$1" == "" ; then
   dt=`date +%Y%m%d`
else
   dt=$1
fi

before_dt_7days=`date -d"$dt -7 day" +"%Y%m%d"`


select mid from
(select mid from mds_bhv_pubblog where dt=$before_dt_7days and is_transmit=0) a
join
(select mid, sum(traned_cnt_total) as traned_cnt_total from mds_tblog_bhv_day where dt>=$before_dt_7days and dt<$dt group by mid) b
on a.mid=b.mid where traned_cnt_total >= 100 ;






select /*+ mapjoin(t1)*/ t2.roomid, t2.mid from 
(select mid from
(select mid from mds_bhv_pubblog where dt=20150903 and is_transmit=0) a
join
(select mid, sum(traned_cnt_total) as traned_cnt_total from mds_tblog_bhv_day where dt>=20150903 and dt<20150910 group by mid) b
on a.mid=b.mid where traned_cnt_total >= 100 ) t1 
join 
(select mid, rootmid from mds_bhv_pubblog  where dt>=before_dt_7days and dt<$dt and is_transmit=1) t2
on t1.mid = t2.rootmid 
;




select count(1) from
(select mid from mds_bhv_pubblog where dt=20150908 and is_transmit=0) a
join
(select mid, traned_cnt_total, traned_num_total from mds_tblog_bhv_day where dt=20150908 and traned_cnt_total=traned_num_total 
) b
on a.mid=b.mid limit 50 ;



