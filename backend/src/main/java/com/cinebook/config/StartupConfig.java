package com.cinebook.config;
import org.springframework.context.annotation.*;import org.springframework.boot.ApplicationRunner;import org.springframework.jdbc.core.JdbcTemplate;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.beans.factory.annotation.Value;
@Configuration public class StartupConfig{
 @Bean ApplicationRunner admin(JdbcTemplate db,PasswordEncoder enc,@Value("${APP_ADMIN_EMAIL:admin@cinebook.local}")String email,@Value("${APP_ADMIN_PASSWORD:Admin@12345}")String password){return args->{Integer n=db.queryForObject("select count(*) from app_user where email=?",Integer.class,email);if(n!=null&&n==0)db.update("insert into app_user(name,email,password_hash,role) values('CineBook Admin',?,?, 'ADMIN')",email,enc.encode(password));};}
}
