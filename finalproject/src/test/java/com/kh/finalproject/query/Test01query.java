package com.kh.finalproject.query;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalproject.dao.LaneDao;
import com.kh.finalproject.dto.CardDto;
import com.kh.finalproject.dto.LaneWithCardsDto;

@SpringBootTest
public class Test01query {

	@Autowired
	private LaneDao laneDao;
	
	@Test
	public void test() {
		List<LaneWithCardsDto> list = laneDao.findLaneWithCards(1);
		for(LaneWithCardsDto laneCardDto : list) {
			System.out.println(laneCardDto.getLaneDto()); 
			for(CardDto card : laneCardDto.getCards()) {
				System.out.println(card);
			}
		}
	}
}
