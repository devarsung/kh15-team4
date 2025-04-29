package com.kh.finalproject.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.finalproject.configuration.TokenProperties;
import com.kh.finalproject.dto.AccountDto;
import com.kh.finalproject.vo.ClaimVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class TokenService {
	
	@Autowired
	private TokenProperties tokenProperties;

	//accessToken 생성
	public String generateAccessToken(AccountDto accountDto) {
		//[1] 키 생성(설정파일에 있음)
		//[2] 시간 계산
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();//현재시각
		c.add(Calendar.MINUTE, tokenProperties.getAccessLimit());
		Date limit = c.getTime();//만료시각
		
		//[3] 토큰 생성
		return Jwts.builder()
				.signWith(tokenProperties.getKey())
				.expiration(limit)
				.issuer(tokenProperties.getIssuer())
				.issuedAt(now)
				.claim("userNo", accountDto.getAccountNo())
				.claim("userEmail", accountDto.getAccountEmail())
			.compact();
	}
	
	//accessToken 해석
	public ClaimVO parse(String token) {
		//토근 문자열, 열쇠 -> 이미 준비됨
		
		//토근 해석
		Claims claims = (Claims) Jwts.parser()
				.verifyWith(tokenProperties.getKey())
				.requireIssuer(tokenProperties.getIssuer())
			.build()
				.parse(token)
				.getPayload();
		
		return ClaimVO.builder()
				.userNo((long) claims.get("userNo"))
				.userEmail((String) claims.get("userEmail"))
			.build();
	}
}
