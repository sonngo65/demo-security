package com.example.demoSecurity.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtils {
	@Value("${application.jwt.secret}")
	private String secret;
	
	private static final long expiration = 100*60*60*24; 
	
	public String generateTokenJwt(String username) {
		return Jwts.builder()
			.setSubject(username)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(signKey(secret))
			.compact();
			
	}
	public String extractUsernameFromToken(String token) {
		
		return extractClaim(token,Claims::getSubject);
	}
	
	public <T> T extractClaim(String token,Function<Claims,T> claimsResolver) {
		final Claims claims = extractClaimsFromToken(token);
		return claimsResolver.apply(claims);
		
		
	}
	
	public Claims extractClaimsFromToken(String token) {
		
		return
		Jwts.parserBuilder()
		.setSigningKey(signKey(secret))
		.build().parseClaimsJws(token)
		.getBody();
		
	}
	
	public boolean isTokenValid(String token) {
		return isTokenExpire(token);
		
		
	}
	public boolean isTokenExpire(String token) {
		
		return extractExpirationToken(token).before(new Date());
	}
	
	public Date extractExpirationToken(String token) {
		return extractClaim(token, Claims::getIssuedAt);
	}
	
	private Key signKey(String secret) {
		byte[] key = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(key);
	}
}
