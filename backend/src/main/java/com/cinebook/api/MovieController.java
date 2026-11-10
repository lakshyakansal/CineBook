package com.cinebook.api;
import java.util.*;import org.springframework.jdbc.core.JdbcTemplate;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/movies") public class MovieController{
 private final JdbcTemplate db;public MovieController(JdbcTemplate d){db=d;}
 @GetMapping List<Map<String,Object>> all(@RequestParam(required=false)String search){String p=search==null?null:"%"+search+"%";return db.queryForList("select * from movie where (? is null or lower(title) like lower(?)) order by release_date desc",search,p);}
 @GetMapping("/{id}") Map<String,Object> one(@PathVariable long id){return db.queryForMap("select * from movie where id=?",id);}
}
