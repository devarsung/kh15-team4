package com.kh.finalproject.query;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dao.LaneDao;
import com.kh.finalproject.dto.CardDto;
import com.kh.finalproject.dto.LaneDto;

@SpringBootTest
public class Test01query {

	@Autowired
	private LaneDao laneDao;
	@Autowired
	private BoardDao boardDao;
	
	@Test
	public void test() {
		List<LaneDto> list = laneDao.selectLaneFullList(1);
		for(LaneDto lane: list) {
			System.out.println(lane);
			for(CardDto card : lane.getCardList()) {
				System.out.println(card);
			}
		}
	}
}
