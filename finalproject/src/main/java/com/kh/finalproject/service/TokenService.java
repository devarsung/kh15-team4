package com.kh.finalproject.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.finalproject.configuration.TokenProperties;
import com.kh.finalproject.dao.AccountTokenDao;
import com.kh.finalproject.dto.AccountDto;
import com.kh.finalproject.dto.AccountTokenDto;
import com.kh.finalproject.error.TargetNotFoundException;
import com.kh.finalproject.vo.ClaimVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class TokenService {
	
	@Autowired
	private TokenProperties tokenProperties;
	@Autowired
	private AccountTokenDao accountTokenDao;

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
	
	//accessToken 생성, 오버로딩
	public String generateAccessToken(ClaimVO claimVO) {
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountNo(claimVO.getUserNo());
		accountDto.setAccountEmail(claimVO.getUserEmail());
		return generateAccessToken(accountDto);
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
				.userNo(((Number) claims.get("userNo")).longValue())
				.userEmail((String) claims.get("userEmail"))
			.build();
	}
	
	//BearerToken 해석
	public ClaimVO parseBearerToken(String bearerToken) {
		//토큰 형태 검사
		if(bearerToken == null) {
			throw new TargetNotFoundException("토큰 없음");
		}
		if(bearerToken.startsWith("Bearer ") == false) {
			throw new TargetNotFoundException("토큰 오류");
		}
		
		String token = bearerToken.substring(7);//"Bearer " 만큼 제거 
		return parse(token);
	}
	
	//Bearer 토큰 발급내역 조회 후 있으면 삭제
	public boolean checkBearerToken(ClaimVO claimVO, String bearerToken) {
		String token = bearerToken.substring(7);
		AccountTokenDto accountTokenDto = accountTokenDao.findByTargetAndToken(
			AccountTokenDto.builder()
					.accountTokenTarget(claimVO.getUserNo())//account_no(=token_target)와
					.accountTokenValue(token)//토큰으로
				.build()
		);//찾아라
		
		if(accountTokenDto != null) {//인증 성공이라면(로그인 갱신 성공)
			//내역 삭제
			accountTokenDao.delete(accountTokenDto);//내역 삭제
			return true;
		}
		
		return false;
	}
	
	//refreshToken 생성
	public String generateRefreshToken(AccountDto accountDto) {
		//시간 계산
		Calendar c = Calendar.getInstance();
		Date now = c.getTime(); //현재시각
		c.add(Calendar.MINUTE, tokenProperties.getRefreshLimit());//2주
		Date limit = c.getTime(); //만료시각
		
		//토큰생성
		String tokenValue = Jwts.builder()
								.signWith(tokenProperties.getKey())
								.expiration(limit)
								.issuer(tokenProperties.getIssuer())
								.issuedAt(now)
								.claim("userNo", accountDto.getAccountNo())
								.claim("userEmail", accountDto.getAccountEmail())
							.compact();
		
		//토큰에 들어갈 정보들을 DB에 저장하는 코드
		AccountTokenDto accountTokenDto = new AccountTokenDto();
		accountTokenDto.setAccountTokenTarget(accountDto.getAccountNo());//발행한 대상
		accountTokenDto.setAccountTokenValue(tokenValue);//발행한 토큰
		accountTokenDao.insert(accountTokenDto);

		//토큰 반환
		return tokenValue;
	}
	
	//refreshToken 생성, 오버로딩
	public String generateRefreshToken(ClaimVO claimVO) {
		return generateRefreshToken(AccountDto.builder()
				.accountNo(claimVO.getUserNo())
				.accountEmail(claimVO.getUserEmail())
			.build());
	}
}
