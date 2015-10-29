

(select uid from mds_bhv_user_active_month_stat where dt='20151018' and stat_type=2 group by uid ) b on a.uid=b.uid join
(select uid from  mds_uquality_user_class where dt='20151018' and (level=1 or level=2) group by uid ) c on a.uid=c.uid ;


dt=$1

#test
dt=`date +"%Y%m%d"`

last_saturday=`date -d "$dt -$(date -d $dt +%u) days - 1 day" +"%Y%m%d"`
week_start=`date -d"$dt -7 day" +"%Y%m%d"`
lastday=`date -d"$dt -1 day" +"%Y%m%d"`
month3=`date -d"$dt -3 month" +"%Y%m%d"`
month2=`date -d"$dt -2 month" +"%Y%m%d"`
month1=`date -d"$dt -1 month" +"%Y%m%d"`

A0="
drop table if exists uid_filter_active_uquality_tmp
create table uid_filter_active_uquality_tmp as
select uid from 
(select uid from mds_bhv_user_active_month_stat where dt='$lastday' and stat_type=2 group by uid ) a
join 
(select uid from  mds_uquality_user_class where dt='$lastday' and (level=1 or level=2) group by uid ) b
on a.uid = b.uid group by a.uid ;
"

A1="
select uid from mds_wls_encode_bhv 
where dt>='$week_start' and dt<'$dt' and action =695 and extend like'%act:follow%'  
and main_id in ('1087030002_2151_5001_9_','1087030002_2151_5001_9_hot','1087030002_2151_5001_9_recently')
and from_val>1053000000 and length(uid)<>13 and from_val like '10%' and (version=0 or version is null)
group by uid ;"


A2="select uid
from mds_wls_encode_bhv 
where dt>='$week_start' and dt<'$dt'  and action=26   
and main_id in ('1087030002_2151_5001_9_','1087030002_2151_5001_9_hot','1087030002_2151_5001_9_recently')
and from_val>1053000000 and length(uid)<>13 and from_val like '10%' and (version=0 or version is null)
group by uid ;"


A3="select cust_uid as uid from ods_ad_bid_reward_log where feed_uid in ('1649159940',
'2092759380',
'1907862885',
'1991713215',
'3226347595',
'2655343743',
'5547543025',
'1773971957',
'1961658655',
'3053243787',
'3495311955')
and dt>='$month3' and dt<'$dt'    
group by uid ;"


A4="select uid from mds_bhv_addatten
where dt>='$week_start' and dt<'$dt' 
and (object rlike '1022:230677' and  substr(object,12,2)<>'sz' and substr(object,12,2)<>'sh' and substr(object,12,2)<>'hk')
group by uid ;"


A5="select uid from mds_wls_encode_bhv 
where dt>='$week_start' and dt<'$dt' and action='26'   
and (main_id rlike '^230677' and substr(main_id,7,2)<>'sz' and substr(main_id,7,2)<>'sh' and substr(main_id,7,2)<>'hk')
group by uid ;"


A6="select a.uid from 
(select uid from ods_tblog_content where dt>='$week_start' and dt<'$dt'  
and (content like '%美股%' 
or content like '%道琼斯%'
or content like '%标普%'
or content like '%纽交所%'
or content like '%纳斯达克%'
or content like '%中概%'
or content like '%熔断%'
or content like '%nasdaq%'
or content like '%QKLS%'
or content like '%KONE%'
or content like '%THTI%'
or content like '%CHNR%'
or content like '%SVM%'
or content like '%CGA%'
or content like '%SCLN%'
or content like '%ZPIN%'
or content like '%OSN%'
or content like '%MCOX%'
or content like '%CLNT%'
or content like '%CCIH%'
or content like '%SGOC%'
or content like '%KGJI%'
or content like '%CO%'
or content like '%JRJC%'
or content like '%XRS%'
or content like '%SINO%'
or content like '%SKBI%'
or content like '%HGSH%'
or content like '%HPJ%'
or content like '%FFHL%'
or content like '%NFEC%'
or content like '%CPGI%'
or content like '%CSUN%'
or content like '%CCSC%'
or content like '%XUE%'
or content like '%NPD%'
or content like '%CYOU%'
or content like '%CADC%'
or content like '%CBPO%'
or content like '%LONG%'
or content like '%CNTF%'
or content like '%WX%'
or content like '%TOUR%'
or content like '%EDS%'
or content like '%CMFO%'
or content like '%RDA%'
or content like '%TBOW%'
or content like '%CNAM%'
or content like '%ADCN%'
or content like '%CCCL%'
or content like '%YTEC%'
or content like '%SHP%'
or content like '%FSIN%'
or content like '%AMAP%'
or content like '%TRIT%'
or content like '%ZA%'
or content like '%COGO%'
or content like '%TUDO%'
or content like '%KEYP.PK%'
or content like '%CDCS%'
or content like '%NINE%'
or content like '%CPSL%'
or content like '%AQ%'
or content like '%VALV%'
or content like '%CEU%'
or content like '%GRRF%'
or content like '%KUN%'
or content like '%CHC%'
or content like '%CTC%'
or content like '%SPRD%'
or content like '%GU%'
or content like '%LAS%'
or content like '%OMEI%'
or content like '%CVVT%'
or content like '%STP%'
or content like '%LEJU%'
or content like '%CHOP%'
or content like '%PACT%'
or content like '%DEER%'
or content like '%SUTR%'
or content like '%HOGS%'
or content like '%WWIN%'
or content like '%LLEN%'
or content like '%SVN%'
or content like '%HRBN%'
or content like '%XING%'
or content like '%LTON%'
or content like '%QXM%'
or content like '%TAOM%'
or content like '%ISS%'
or content like '%CALI%'
or content like '%MEMS%'
or content like '%CMGE%'
or content like '%ABAT%'
or content like '%CBP%'
or content like '%MONT%'
or content like '%CNET%'
or content like '%SCOK%'
or content like '%ADY%'
or content like '%JNGW%'
or content like '%SHZ%'
or content like '%GA%'
or content like '%AMBO%'
or content like '%ZHIC%'
or content like '%NEWN%'
or content like '%GDCT%'
or content like '%AOBI%'
or content like '%UTRA%'
or content like '%CDII%'
or content like '%ZSTN%'
or content like '%NKBP%'
or content like '%GPRC%'
or content like '%ASIA%'
or content like '%CFSG%'
or content like '%NQ%'
or content like '%CSKI%'
or content like '%SOL%'
or content like '%GSI%'
or content like '%VIT%'
or content like '%CHDX%'
or content like '%CTFO%'
or content like '%BCDS%'
or content like '%LASO%'
or content like '%CHNG%'
or content like '%ONP%'
or content like '%CXDC%'
or content like '%HLG%'
or content like '%CHRM%'
or content like '%HOGS%'
or content like '%WWIN%'
or content like '%LLEN%'
or content like '%QXM%'
or content like '%SVN%'
or content like '%HRBN%'
or content like '%XING%'
or content like '%LTON%'
or content like '%TAOM%'
or content like '%ISS%'
or content like '%CALI%'
or content like '%MEMS%'
or content like '%CMGE%'
or content like '%SCOK%'
or content like '%ABAT%'
or content like '%CBP%'
or content like '%MONT%'
or content like '%CNET%'
or content like '%ADY%'
or content like '%JNGW%'
or content like '%SHZ%'
or content like '%GA%'
or content like '%AMBO%'
or content like '%ZHIC%'
or content like '%NEWN%'
or content like '%GDCT%'
or content like '%AOBI%'
or content like '%UTRA%'
or content like '%CDII%'
or content like '%ZSTN%'
or content like '%CRIC%'
or content like '%SNDA%'
or content like '%KONG%'
or content like '%NSB%'
or content like '%SORL%'
or content like '%CHGS%'
or content like '%OINK%'
or content like '%SSRX%'
or content like '%HEAT%'
or content like '%BEST%'
or content like '%WH%'
or content like '%LDK%'
or content like '%SUNG%'
or content like '%WUHN%'
or content like '%LIWA%'
or content like '%CIS%'
or content like '%PWRD%'
or content like '%BWOW%'
or content like '%LPH%'
or content like '%HTHT%'
or content like '%HMIN%'
or content like '%DQ%'
or content like '%XNET%'
or content like '%KNDI%'
or content like '%CCCR%'
or content like '%GSOL%'
or content like '%WOWO%'
or content like '%VIMC%'
or content like '%VNET%'
or content like '%JKS%'
or content like '%TEDU%'
or content like '%JASO%'
or content like '%XNY%'
or content like '%CAAS%'
or content like '%BSPM%'
or content like '%XIN%'
or content like '%SMI%'
or content like '%CYD%'
or content like '%KZ%'
or content like '%GAME%'
or content like '%STV%'
or content like '%ATAI%'
or content like '%MY%'
or content like '%ATV%'
or content like '%MR%'
or content like '%FENG%'
or content like '%SPU%'
or content like '%MOMO%'
or content like '%CMCM%'
or content like '%GRO%'
or content like '%NTES%'
or content like '%CSIQ%'
or content like '%RCON%'
or content like '%CNYD%'
or content like '%BONA%'
or content like '%CCM%'
or content like '%JST%'
or content like '%CJJD%'
or content like '%EHIC%'
or content like '%CNIT%'
or content like '%GOMO%'
or content like '%SVA%'
or content like '%BIDU%'
or content like '%OIIM%'
or content like '%JP%'
or content like '%CHLN%'
or content like '%EJ%'
or content like '%TSL%'
or content like '%SEED%'
or content like '%AMCN%'
or content like '%HOLI%'
or content like '%BZUN%'
or content like '%JOBS%'
or content like '%AXN%'
or content like '%SYUT%'
or content like '%YGE%'
or content like '%CISG%'
or content like '%EDU%'
or content like '%TPI%'
or content like '%CTRP%'
or content like '%CREG%'
or content like '%JMEI%'
or content like '%WUBA%'
or content like '%DATE%'
or content like '%SOHU%'
or content like '%NOAH%'
or content like '%MOBI%'
or content like '%BORN%'
or content like '%YY%'
or content like '%DHRM%'
or content like '%SFUN%'
or content like '%WBAI%'
or content like '%QIHU%'
or content like '%BABA%'
or content like '%LITB%'
or content like '%DANG%'
or content like '%QUNR%'
or content like '%YOKU%'
or content like '%BITA%'
or content like '%SINA%'
or content like '%YZC%'
or content like '%RENN%'
or content like '%NEP%'
or content like '%GURE%'
or content like '%SKYS%'
or content like '%WB%'
or content like '%ATHM%'
or content like '%VISN%'
or content like '%DL%'
or content like '%NCTY%'
or content like '%JD%'
or content like '%KUTV%'
or content like '%VIPS%'
or content like '%ALN%'
or content like '%CBAK%'
or content like '%CPHI%'
or content like '%TSTC%'
) ) a
join
(select uid from  mds_uquality_user_class where dt ='$lastday' and level in ('1','2')) b on a.uid=b.uid
group by a.uid ;"


A7="select split(from_id,':')[1] as uid from mds_user_inte_tag 
where to_id in ('1042015:abilityTag_110') 
and dt ='$last_saturday'
group by split(from_id,':')[1] ;"


A8="select uid from mds_wls_encode_bhv where dt>='$week_start' and dt<'$dt' and action='695'  
and target_id='230771_-_NEW_HANGQING_SECOND_PAGE_US'  
group by uid ;"


A9="select a.uid from
(select uid
from mds_wls_user_android_app_all_new 
where status<>'1' and dt ='$lastday'  and substr(update_time,0,10)>='{dt3}' and substr(update_time,0,10)<='{dt4}'    /*近三月*/
and app_name = '雪球') a
join
(select uid,level from  mds_uquality_user_class where dt ='$lastday' ) b on a.uid=b.uid 
group by a.uid ;"


A10="select a.uid from
(select fans_uid as uid from mds_user_fanslist where uid in ('1649159940',
'2092759380',
'1907862885',
'1991713215',
'3226347595',
'2655343743',
'5547543025',
'1773971957',
'1961658655',
'3053243787',
'3495311955') 
and dt ='$lastday' ) a
join
(select uid from  mds_uquality_user_class where dt ='$lastday' and level in ('1','2')) b on a.uid=b.uid
group by a.uid ;"



hql="
insert overwrite table weibo_stock_user_us_push partition(dt='$dt')
select uid from 
(select uid from weibo_stock_user_us where dt='$dt' group by uid
 UNION ALL
 select uid from weibo_stock_user_us_push where dt = ''
 ) a
join
(select uid from mds_bhv_user_active_month_stat where dt='$lastday' and stat_type=2 group by uid ) b 
on a.uid=b.uid 
join
(select uid from  mds_uquality_user_class where dt='$lastday' and (level=1 or level=2) group by uid ) c 
on a.uid=c.uid ;
" 

CREATE  TABLE `weibo_stock_user_us`(
  `uid` string)
PARTITIONED BY ( 
  `type` string, 
  `dt` string)
STORED AS RCFILE
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/weibo_stock_user_us' ;


CREATE  TABLE `weibo_stock_user_us_push`(
  `uid` string)
PARTITIONED BY (  
  `dt` string)
STORED AS RCFILE
LOCATION 'hdfs://ns1/user/wb_wls_data/warehouse/weibo_stock_user_us_push' ;

grant select on table weibo_stock_user_us to user yuping6;
grant select on table weibo_stock_user_us_push to user yuping6;

