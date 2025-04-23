package com.kh.finalproject.query;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dto.CardColumnDto;
import com.kh.finalproject.dto.CardDto;

@SpringBootTest
public class Test01query {

	@Autowired
	private BoardDao boardDao;
	
	@Test
	public void test() {
		List<CardColumnDto> list = boardDao.test();
		for(CardColumnDto ccd : list) {
			System.out.println(ccd.getColumnDto());
			for(CardDto card : ccd.getCards()) {
				System.out.println(card);
			}
		}
	}
}
