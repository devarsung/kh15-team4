package com.kh.finalproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dao.LaneDao;
import com.kh.finalproject.dto.BoardDto;
import com.kh.finalproject.dto.LaneFullDto;
import com.kh.finalproject.websocket.MessageResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardService {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private LaneDao laneDao;
	
	public void sendMessage(long boardNo) {
		MessageResponseVO response = new MessageResponseVO();
		response.setType("put");
		
		//조회 후 모두에게 메세지 전송
		List<LaneFullDto> list = laneDao.selectLaneFullList(boardNo);
		response.setData(list);
		messagingTemplate.convertAndSend("/private/update/" + boardNo, response);
	}
	
	public void sendBoardInfo(long boardNo) {
		MessageResponseVO response = new MessageResponseVO();
		response.setType("patch");
		BoardDto boardDto = boardDao.selectOne(boardNo);
		response.setData(boardDto);
		messagingTemplate.convertAndSend("/private/update/" + boardNo, response);
	}
}
