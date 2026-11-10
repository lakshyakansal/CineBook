package com.cinebook.api;
import java.util.*;import org.springframework.jdbc.core.JdbcTemplate;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/shows") public class ShowController{
 private final JdbcTemplate db;public ShowController(JdbcTemplate d){db=d;}
 @GetMapping List<Map<String,Object>> list(@RequestParam long movieId,@RequestParam(required=false)String date){String q="select st.id,st.starts_at,st.base_price,t.name theatre_name,t.city,sc.name screen_name from show_time st join screen sc on sc.id=st.screen_id join theatre t on t.id=sc.theatre_id where st.movie_id=? and st.starts_at>now()";if(date!=null)q+=" and st.starts_at::date=?::date";q+=" order by st.starts_at";return date==null?db.queryForList(q,movieId):db.queryForList(q,movieId,date);}
 @GetMapping("/{id}/seats") List<Map<String,Object>> seats(@PathVariable long id){return db.queryForList("select cs.id,cs.row_label,cs.seat_number,cs.category,exists(select 1 from booking_seat bs join booking b on b.id=bs.booking_id where bs.seat_id=cs.id and b.show_id=? and b.status='CONFIRMED') booked from show_time st join cinema_seat cs on cs.screen_id=st.screen_id where st.id=? order by cs.row_label,cs.seat_number",id,id);}
}
