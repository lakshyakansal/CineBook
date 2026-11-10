package com.cinebook.security;
import io.jsonwebtoken.*;import io.jsonwebtoken.security.Keys;import javax.crypto.SecretKey;import java.nio.charset.StandardCharsets;import java.util.Date;import org.springframework.beans.factory.annotation.Value;import org.springframework.stereotype.Service;
@Service public class JwtService{
 private final SecretKey key;private final long expiry;
 public JwtService(@Value("${app.jwt.secret}")String s,@Value("${app.jwt.expiration-ms}")long e){if(s.getBytes(StandardCharsets.UTF_8).length<32)throw new IllegalArgumentException("JWT secret must be 32+ bytes");key=Keys.hmacShaKeyFor(s.getBytes(StandardCharsets.UTF_8));expiry=e;}
 public String create(long id,String email,String role){return Jwts.builder().subject(email).claim("uid",id).claim("role",role).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+expiry)).signWith(key).compact();}
 public Claims parse(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();}
}
