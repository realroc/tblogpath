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


exec_hql="
drop table if exists kol_tmp_uid_mid;
create table kol_tmp_uid_mid as 
select a.uid, mid, level from
    (select uid ,mid from mds_bhv_pubblog where dt>=$first_day_lastmonth and dt<=$last_day_lastmonth
        union all
    select rootuid as uid, mid from mds_bhv_pubblog where  dt>=$first_day_lastmonth and dt<=$last_day_lastmonth and is_transmit='1') a
    join
    (select uid, level from mds_uquality_user_class where dt='$last_day_lastmonth' and (level='1' or level='2')) b
    on a.uid = b.uid ;
"

#echo $exec_hql
#hive -e "$exec_hql"

exec_hql="
insert overwrite table kol_tmp_expo partition(mt='$lastmonth')
select uid, level, sum(expo_cnt) as expo_cnt from 
    (select y.uid, level, expo_cnt from 
        (select mid, appid , sum(expo_cnt ) as expo_cnt from mds_tblog_expo_day where dt>=$first_day_lastmonth and dt<=$last_day_lastmonth and interface_id <>'253' group by mid, appid ) x 
        join 
        kol_tmp_uid_mid y 
        on x.mid = y.mid 
        join 
        (select appid from ods_dim_appkey where dt=$last_day_lastmonth and permission_mast_code='1') z 
            on x.appid = z.appid
) t group by uid, level having expo_cnt > 100000 ;
"
echo $first_day_lastmonth
echo $last_day_lastmonth
echo $exec_hql
hive -e "$exec_hql" 