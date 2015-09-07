

w
theday

select c.rootmid, c.mid, c.parentmid from 
(select mid from mds_bhv_pubblog where dt = $weekago and is_transmit = 0 ) a 
join 
(select mid, sum(traned_cnt_total) as traned_cnt_total from mds_tblog_bhv_day where dt > '' and dt <= '' group by mid having traned_cnt_total >= 100) b
on a.mid = b.mid  
join 
(select rootmid, mid, parentmid from mds_bhv_pubblog where dt >= and dt<=) c
on a.mid = c.rootmid ;