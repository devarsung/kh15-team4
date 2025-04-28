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

@CrossOrigin
@RestController
@RequestMapping("/api/account")
public class AccountRestController {

	@Autowired
	private AccountDao accountDao;
	
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
}
