package com.cinebook.security;
import jakarta.servlet.*;import jakarta.servlet.http.*;import java.io.IOException;import java.util.List;import org.springframework.stereotype.Component;import org.springframework.web.filter.OncePerRequestFilter;import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;import org.springframework.security.core.authority.SimpleGrantedAuthority;import org.springframework.security.core.context.SecurityContextHolder;
@Component public class JwtFilter extends OncePerRequestFilter{
 private final JwtService jwt;public JwtFilter(JwtService j){jwt=j;}
 protected void doFilterInternal(HttpServletRequest q,HttpServletResponse s,FilterChain c)throws ServletException,IOException{String h=q.getHeader("Authorization");if(h!=null&&h.startsWith("Bearer "))try{var claims=jwt.parse(h.substring(7));var a=new UsernamePasswordAuthenticationToken(claims.getSubject(),null,List.of(new SimpleGrantedAuthority("ROLE_"+claims.get("role",String.class))));a.setDetails(claims.get("uid"));SecurityContextHolder.getContext().setAuthentication(a);}catch(Exception e){SecurityContextHolder.clearContext();}c.doFilter(q,s);}
}
