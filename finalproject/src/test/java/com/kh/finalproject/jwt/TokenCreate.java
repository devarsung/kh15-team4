package com.kh.finalproject.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class TokenCreate {

	@Test
	public void test() {
		String keyString = "abcdefghijklmnopqrstuvwxyz1234567890";
		byte[] a = keyString.getBytes(StandardCharsets.UTF_8);
		SecretKey key = Keys.hmacShaKeyFor(keyString.getBytes(StandardCharsets.UTF_8));
		
		log.debug("keyString={}", keyString);
		log.debug("a={}", a);
		log.debug("key={}", key);
		
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();//현재 시각
		
		c.add(Calendar.MINUTE, 1);//1분뒤 만료
		Date limit = c.getTime();//만료 시각
		
		String token = Jwts.builder()
					.signWith(key)
					.expiration(limit)
					.issuer("mymy")
					.issuedAt(now)
					.claim("userId", "shinjjanggu")
					.claim("userLevel", "우수회원")
				.compact();
		
		log.debug("token={}", token);
	}
}
