package com.kh.finalproject.query;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalproject.dao.AccountDao;
import com.kh.finalproject.dto.AccountDto;

@SpringBootTest
public class Test03DummyAccount {
	
	@Autowired
	private AccountDao accountDao;
	
	@Test
	public void test() {
//		AccountDto accountDto = AccountDto.builder().accountEmail("kimdevelop@naver.com").accountPw("helloworld").build();
//		accountDao.insert(accountDto);
		
		for (int i = 1; i <= 100; i++) {
	        String email = "user" + i + "@example.com";
	        String password = "helloworld";

	        AccountDto accountDto = AccountDto.builder()
	            .accountEmail(email)
	            .accountPw(password)
	            .build();

	        accountDao.insert(accountDto);
	    }
	}
}
