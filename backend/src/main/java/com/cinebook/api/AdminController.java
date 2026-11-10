package com.cinebook.api;
import jakarta.validation.Valid;import jakarta.validation.constraints.*;import java.util.*;import org.springframework.jdbc.core.JdbcTemplate;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin") public class AdminController{
 private final JdbcTemplate db;public AdminController(JdbcTemplate d){db=d;}
 record MovieInput(@NotBlank String title,String description,String posterUrl,String language,String genre,@Min(1)int durationMinutes,String certification,String status){}
 record ShowInput(@Positive long movieId,@Positive long screenId,@NotBlank String startsAt,@Positive double basePrice){}
 @GetMapping("/dashboard") Map<String,Object> dashboard(){return Map.of("movies",db.queryForObject("select count(*) from movie",Long.class),"bookings",db.queryForObject("select count(*) from booking",Long.class),"users",db.queryForObject("select count(*) from app_user",Long.class),"revenue",db.queryForObject("select coalesce(sum(total_amount),0) from booking where status='CONFIRMED'",java.math.BigDecimal.class));}
 @PostMapping("/movies") Map<String,Object> movie(@Valid @RequestBody MovieInput m){return db.queryForMap("insert into movie(title,description,poster_url,language,genre,duration_minutes,certification,status) values(?,?,?,?,?,?,?,?) returning *",m.title(),m.description(),m.posterUrl(),m.language(),m.genre(),m.durationMinutes(),m.certification(),m.status()==null?"NOW_SHOWING":m.status());}
 @GetMapping("/theatres") List<Map<String,Object>> theatres(){return db.queryForList("select t.id,t.name,t.city,t.address,s.id screen_id,s.name screen_name from theatre t join screen s on s.theatre_id=t.id");}
 @PostMapping("/shows") Map<String,Object> show(@Valid @RequestBody ShowInput s){return db.queryForMap("insert into show_time(movie_id,screen_id,starts_at,base_price) values(?,?,?::timestamptz,?) returning *",s.movieId(),s.screenId(),s.startsAt(),s.basePrice());}
 @GetMapping("/bookings") List<Map<String,Object>> bookings(){return db.queryForList("select b.booking_code,b.status,b.total_amount,u.email,m.title,st.starts_at from booking b join app_user u on u.id=b.user_id join show_time st on st.id=b.show_id join movie m on m.id=st.movie_id order by b.created_at desc limit 300");}
}
