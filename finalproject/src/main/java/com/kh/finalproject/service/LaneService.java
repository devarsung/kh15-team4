package com.kh.finalproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.finalproject.dao.LaneDao;
import com.kh.finalproject.dto.CardDto;
import com.kh.finalproject.dto.LaneDto;
import com.kh.finalproject.dto.LaneFullDto;
import com.kh.finalproject.vo.LaneCardMapVO;

@Service
public class LaneService {
	
	@Autowired
	private LaneDao laneDao;

	public LaneCardMapVO convertData(List<LaneFullDto> laneFullDtoList) {
		LaneCardMapVO laneCardMapVO = new LaneCardMapVO();

		for(LaneFullDto laneFullDto : laneFullDtoList) {
			LaneDto laneDto = laneFullDto.getLaneDto();
			List<CardDto> cardList = laneFullDto.getCardList();
			
			//order 제외한 필드 필요
		}
		
		laneCardMapVO.setLaneMap(null);
		laneCardMapVO.setCardMap(null);
		return null;
		
	}
}
