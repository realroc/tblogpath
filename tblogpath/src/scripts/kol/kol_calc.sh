
if test "$1" == "" ; then
   dt=`date +%Y%m%d`
else
   dt=$1
fi

first_day_month=`date -d"$dt" "+%Y%m01"`
six_month_ago=`date -d"$first_day_month -6 month" +"%Y%m"`
lastmonth=`date -d"$first_day_month -1 month" +"%Y%m"`
last_day_lastmonth=`date -d"$first_day_month -1 day" +"%Y%m%d"` 
first_day_sixmonth=`date -d"$first_day_month -6 month" +"%Y%m%d"`


##计算连续6个月曝光数大于10万的用户
exec_hql="
drop table if exists kol_influence_uid;
create table kol_influence_uid as 
select /*+ mapjoin(a)*/ a.uid, level, nick from
(select uid, level, count(1) from kol_tmp_expo where mt>=$six_month_ago and mt<=$lastmonth group by uid, level having count(1) = 6) a 
join 
(select uid, nick from mds_user_info where dt=$last_day_lastmonth) b
on a.uid = b.uid ;
"

echo $six_month_ago
echo $lastmonth
echo $exec_hql
echo $last_day_lastmonth

echo "$exec_hql"
hive -e "$exec_hql" 



##计算六个月用户互动数
exec_hql="
drop table if exists kol_influence_uid_hudong;
create table kol_influence_uid_hudong as
select x.uid, if(hudong_num is null or hudong_num ='',0,hudong_num) as hudong_num from
 kol_influence_uid x
 left outer join
 (
select  a.uid,count(1) as hudong_num ,
        sum(case when tag='tran' then 1 else 0 end) as tran ,
        sum(case when tag='cmt' then 1 else 0 end) as cmt,
        sum(case when tag='zan' then 1 else 0 end) as zan  from
        (
         select /*+ mapjoin(s1)*/ s1.uid, mid from kol_influence_uid s1
                join
         (select uid, mid from mds_bhv_pubblog where dt>=$first_day_sixmonth and dt<=$last_day_lastmonth) s2
         on s1.uid = s2.uid) a
        join
        (
        select mid,uid as cuid ,'tran' as tag from mds_bhv_pubblog where dt>=$first_day_sixmonth and dt<=$last_day_lastmonth and is_transmit='1'
        union all
        select  mid,uid as cuid,'cmt' as tag from mds_bhv_cmtblog where dt>=$first_day_sixmonth and dt<=$last_day_lastmonth
        union all
        select object_id as mid , uid as cuid ,'zan' as tag  from mds_bhv_like where dt>=$first_day_sixmonth and dt<=$last_day_lastmonth and status='1' and mode='1'
        ) b
on a.mid=b.mid
group by uid ) y
on x.uid=y.uid ;
"

echo "$exec_hql"
hive -e "$exec_hql"




##计算用户最近粉丝数 
exec_hql="
drop table if exists kol_influence_uid_funs;
create table kol_influence_uid_funs as
select uid, 
       filtered_fans_num, 
       round(case when filtered_fans_num=0 or fans_num =0 then 0 else filtered_fans_num/fans_num end ,6) as filtered_fans_rate 
       from 
        (select a.uid, 
                if(filtered_fans_num is null or filtered_fans_num ='',0,filtered_fans_num) as filtered_fans_num ,
                if(fans_num is null or fans_num ='',0,fans_num) as fans_num 
                from 
                kol_influence_uid a 
                left outer join 
                (select uid,filtered_fans_num, fans_num from mds_user_relation_sum where dt=$last_day_lastmonth and user_type_id='2') b 
                on a.uid=b.uid 
        ) c  ;
"

echo "$exec_hql"
hive -e "$exec_hql"





##指标处理，取相关指标取log10
exec_hql="
drop table if exists kol_influence_result_prepare;
create table kol_influence_result_prepare as
select /*+ mapjoin(a)*/ a.uid, 
           level, 
           nick, 
           case when expo=0 then 0 else log10(expo) end as log10_expo, 
           case when hudong_num=0 then 0 else log10(hudong_num) end as log10_hudong,
           case when filtered_fans_num=0 then 0 else log10(filtered_fans_num) end as log10_filtered_fans,
           filtered_fans_rate from 
kol_influence_uid a
join
(select uid, (sum(expo_num)/6) as expo from kol_tmp_expo where mt>=$six_month_ago and mt<=$lastmonth group by uid) b 
on a.uid = b.uid
join 
(select uid, hudong_num/6 as hudong_num from kol_influence_uid_hudong) c 
on a.uid = c.uid
join
(select uid, filtered_fans_num, filtered_fans_rate from kol_influence_uid_funs ) d 
on a.uid = d.uid
"
echo "$exec_hql"
hive -e "$exec_hql"



##计算最终得分
exec_hql="
drop table if exists kol_influence_result;
create table kol_influence_result as
select uid, level, nick, expo_count*0.6 + hudong_count*0.2 + fans_count*0.2 as score
from(
select uid, level, nick, (log10_expo - min_log10_expo)/(max_log10_expo - min_log10_expo) * 1000 as expo_count, 
                   (log10_hudong - min_log10_hudong)/(max_log10_hudong - min_log10_hudong) * 1000 as hudong_count,
                   case when filtered_fans_rate * 1000 < 500 then 0
                   else ((log10_filtered_fans - min_log10_filtered_fans)/(max_log10_filtered_fans - min_log10_filtered_fans) * 1000 + filtered_fans_rate * 1000)/2
                   end as fans_count
from kol_influence_result_prepare a
join
(select '$dt' as dt, max(log10_expo) as max_log10_expo, min(log10_expo) as min_log10_expo, max(log10_hudong) as max_log10_hudong, min(log10_hudong) as min_log10_hudong,
max(log10_filtered_fans) as max_log10_filtered_fans, min(log10_filtered_fans) as min_log10_filtered_fans
from kol_influence_result_prepare ) b
on b.dt=$dt
) t
"
echo "$exec_hql"
hive -e "$exec_hql"