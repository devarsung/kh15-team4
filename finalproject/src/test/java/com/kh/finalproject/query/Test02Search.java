package com.kh.finalproject.query;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalproject.dao.AccountDao;
import com.kh.finalproject.dto.AccountDto;
import com.kh.finalproject.vo.UserSearchVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class Test02Search {
	@Autowired
	private AccountDao accountDao;
	
	@Test
	public void test() {
		UserSearchVO vo = new UserSearchVO();
		vo.setKeyword("deva");
		List<AccountDto> list = accountDao.complexSearch(vo);
		log.debug("list = {}", list);
	}
}
