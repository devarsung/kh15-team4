package com.kh.finalproject.query;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dao.LaneDao;
import com.kh.finalproject.dto.CardFullDto;
import com.kh.finalproject.dto.LaneFullDto;

@SpringBootTest
public class Test01query {

	@Autowired
	private LaneDao laneDao;
	@Autowired
	private BoardDao boardDao;
	
	@Test
	public void test() {
		List<LaneFullDto> list = laneDao.selectLaneFullList(1);
		for(LaneFullDto laneFullDto: list) {
			System.out.println(laneFullDto);
			for(CardFullDto card : laneFullDto.getCardList()) {
				System.out.println(card);
			}
		}
	}
}
