package com.kh.finalproject.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalproject.dao.AccountDao;
import com.kh.finalproject.dto.AccountDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class JoinAndLoginTest {
	
	@Autowired
	private AccountDao accountDao;

	@Test
	public void test() {
//		AccountDto accountDto = new AccountDto();
//		accountDto.setAccountEmail("devarsung@gmail.com");
//		accountDto.setAccountPw("helloworld");
//		accountDto.setAccountNickname("도라도라");
//		
//		accountDao.insert(accountDto);
		
		AccountDto findDto = accountDao.login(AccountDto.builder()
					.accountEmail("devarsung@gmail.com")
					.accountPw("helloworld")
				.build());
		
		log.debug("findDto = {}",findDto);
	}
}
