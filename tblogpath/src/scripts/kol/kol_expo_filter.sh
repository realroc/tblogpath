
if test "$1" == "" ; then
   dt=`date +%Y%m%d`
else
   dt=$1
fi

first_day_month=`date -d"$dt" "+%Y%m01"`
first_day_lastmonth=`date -d"$first_day_month -1 month" +"%Y%m%d"` 
last_day_lastmonth=`date -d"$first_day_month -1 day" +"%Y%m%d"` 
lastmonth=`date -d"$first_day_month -1 month" +"%Y%m"`

#test
#first_day_lastmonth=20150908
#last_day_lastmonth=20150908
#lastmonth=201508

##原创及直接转发博文(自身博文）
exec_hql="
insert overwrite table kol_tmp_uid_mid partition(is_transmit=0)
select a.uid, mid, level from
    (select uid ,mid from mds_bhv_pubblog where dt>=$first_day_lastmonth and dt<=$last_day_lastmonth) a
    join
    (select uid, level from mds_uquality_user_class where dt='$last_day_lastmonth' and (level='1' or level='2')) b
    on a.uid = b.uid ;
"

echo $exec_hql
hive -e "$exec_hql"


##转发后的新博文
exec_hql="
insert overwrite table kol_tmp_uid_mid partition(is_transmit=1)
select b.uid, a.mid, level from
    (select mid ,rootmid from mds_bhv_pubblog where dt>=$first_day_lastmonth and dt<=$last_day_lastmonth and is_transmit=1) a
    join
    (select uid, mid, level from kol_tmp_uid_mid where is_transmit=0) b
    on a.rootmid=b.mid;
"

echo $exec_hql
hive -e "$exec_hql"


exec_hql="
insert overwrite table kol_tmp_expo partition(mt='$lastmonth')
select uid, level, sum(expo_cnt) as expo_cnt_sum from 
    (select uid, level, mid from kol_tmp_uid_mid group by uid, level, mid) t1
    join
    (select /*+ mapjoin(s1)*/ mid, sum(expo_cnt) 
    from 
        (select appid from ods_dim_appkey where dt=$last_day_lastmonth and permission_mast_code='1') s1
        join 
        (select mid, appid, expo_cnt from mds_tblog_expo_day where dt>=$first_day_lastmonth and dt<=$last_day_lastmonth and interface_id <>'253') s2
        on s1.appid = s2.appid group by mid
    ) t2 
    on t1.mid = t2.mid group by uid, level having expo_cnt_sum > 100000 ;
"
echo $first_day_lastmonth
echo $last_day_lastmonth
echo $exec_hql
hive -e "$exec_hql" 