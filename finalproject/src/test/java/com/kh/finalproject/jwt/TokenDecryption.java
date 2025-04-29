package com.kh.finalproject.jwt;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class TokenDecryption {
	
	@Test
	public void test() {
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3NDU5MDgzMTEsImlzcyI6Im15bXkiLCJpYXQiOjE3NDU5MDgyNTEsInVzZXJJZCI6InNoaW5qamFuZ2d1IiwidXNlckxldmVsIjoi7Jqw7IiY7ZqM7JuQIn0.Wc60L7COfGwGyMJhuoF3biPACZeUWrXvnh2H8r9ZDvk";
			
		String keyString = "abcdefghijklmnopqrstuvwxyz1234567890";
		SecretKey key = Keys.hmacShaKeyFor(keyString.getBytes(StandardCharsets.UTF_8));
		
		Claims claims = (Claims)Jwts.parser()
			.verifyWith(key)
			.requireIssuer("mymy")
			.build()
			.parse(token)
			.getPayload();
		
		String userId = (String) claims.get("userId");
		log.debug("userId = {}" , userId);
		String userLevel = (String) claims.get("userLevel");
		log.debug("userLevel = {}", userLevel);
	}

	
	
	
	
}
