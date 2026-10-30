package com.becoder.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final SecretKey signingKey;
	private final long expirationMillis;

	public JwtService(@Value("${JWT_SECRET}") String secret,@Value("${jwt.expiration:86400000}") long expirationMillis) {
		
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.signingKey = Keys.hmacShaKeyFor(keyBytes);
		this.expirationMillis = expirationMillis;
	}

	public String generateToken(UserDetails userDetails) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationMillis);
		
		return Jwts.builder().subject(userDetails.getUsername()).issuedAt(now).expiration(expiry).signWith(signingKey).compact();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		Claims claims = extractAllClaims(token);
		return claims.getSubject().equals(userDetails.getUsername()) && claims.getExpiration().after(new Date());
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
	}
}
