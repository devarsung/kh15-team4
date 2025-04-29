package com.kh.finalproject.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalproject.dao.AccountDao;
import com.kh.finalproject.dto.AccountDto;
import com.kh.finalproject.error.TargetNotFoundException;
import com.kh.finalproject.service.TokenService;
import com.kh.finalproject.vo.SingInResponseVO;

@CrossOrigin
@RestController
@RequestMapping("/api/account")
public class AccountRestController {

	@Autowired
	private AccountDao accountDao;
	@Autowired
	private TokenService tokenService;
	
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
			.build();
	}
}
