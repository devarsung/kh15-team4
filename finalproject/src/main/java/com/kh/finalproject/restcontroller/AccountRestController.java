package com.kh.finalproject.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalproject.dao.AccountDao;
import com.kh.finalproject.dao.AccountTokenDao;
import com.kh.finalproject.dto.AccountDto;
import com.kh.finalproject.error.TargetNotFoundException;
import com.kh.finalproject.service.TokenService;
import com.kh.finalproject.vo.ClaimVO;
import com.kh.finalproject.vo.SingInResponseVO;

@CrossOrigin
@RestController
@RequestMapping("/api/account")
public class AccountRestController {

	@Autowired
	private AccountDao accountDao;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private AccountTokenDao accountTokenDao;
	
	//계정생성
	@PostMapping("/")
	public void join(@RequestBody AccountDto accountDto) {
		accountDao.insert(accountDto);
	}
	
	//존재하는 이메일인가
	@GetMapping("/accountEmail/{accountEmail}")
	public boolean findByEmail(@PathVariable String accountEmail) {
		AccountDto findDto = accountDao.selectOne(accountEmail);
		return findDto == null ? false : true;
	}
	
	//로그인
	@PostMapping("/login")
	public SingInResponseVO login(@RequestBody AccountDto accountDto) {
		//DB 탐색
		AccountDto findDto = accountDao.login(accountDto);
		if(findDto == null) throw new TargetNotFoundException("정보 불일치");
		
		//로그인 성공
		//사용자가 받아야 할 데이터를 생성하고 반환하기
		return SingInResponseVO.builder()
				.userNo(findDto.getAccountNo())
				.userEmail(findDto.getAccountEmail())
				.accessToken(tokenService.generateAccessToken(findDto))
				.refreshToken(tokenService.generateRefreshToken(findDto))
			.build();
	}
	
	//로그아웃
	@PostMapping("/logout")
	public void logout(@RequestHeader("Authorization") String accessToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		accountTokenDao.clean(claimVO.getUserNo());
	}
	
	//로그인 갱신
	@PostMapping("/refresh")
	public SingInResponseVO refresh(@RequestHeader("Authorization") String refreshToken) {
		//refreshToken이 없거나 Bearer 토큰이 아니면 차단
		//토큰을 해석하여 claimVO 추출
		ClaimVO claimVO = tokenService.parseBearerToken(refreshToken);
		
		//userNo와 refreshToken을 이용해서 발급내역 조회(DB 조회)
		//발급내역이 있으면 삭제하기
		boolean isValid = tokenService.checkBearerToken(claimVO, refreshToken);
		
		if(isValid == false) {
			throw new TargetNotFoundException("정보 불일치");
		}
		
		//신규 로그인 정보 재발행 및 반환(accessToken, refreshToken 재발행)
		return SingInResponseVO.builder()
				.userNo(claimVO.getUserNo())
				.userEmail(claimVO.getUserEmail())
				.accessToken(tokenService.generateAccessToken(claimVO))
				.refreshToken(tokenService.generateRefreshToken(claimVO))
			.build();
	}
}
