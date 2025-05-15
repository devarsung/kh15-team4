package com.kh.finalproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.kh.finalproject.dao.LaneDao;
import com.kh.finalproject.dto.LaneFullDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardService {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private LaneDao laneDao;
	
	public void sendMessage(long boardNo) {
		//조회 후 모두에게 메세지 전송
		List<LaneFullDto> response = laneDao.selectLaneFullList(boardNo);
		messagingTemplate.convertAndSend("/private/update/" + boardNo, response);
	}
}
