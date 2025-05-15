package com.kh.finalproject.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalproject.dao.LaneDao;
import com.kh.finalproject.dto.LaneDto;
import com.kh.finalproject.dto.LaneFullDto;
import com.kh.finalproject.error.TargetNotFoundException;
import com.kh.finalproject.service.BoardService;

@CrossOrigin
@RestController
@RequestMapping("/api/lane")
public class LaneRestController {
	
	@Autowired
	private LaneDao laneDao;
	@Autowired
	private BoardService boardService;
	
	//보드의 레인 리스트
	@GetMapping("/{boardNo}")
	public List<LaneDto> list(@PathVariable long boardNo) {
		return laneDao.selectLaneList(boardNo);
	}
	
	//보드의 레인(카드포함) 리스트
	@GetMapping("/lanefull/{boardNo}") 
	public List<LaneFullDto> laneFullList(@PathVariable long boardNo) {
		return laneDao.selectLaneFullList(boardNo);
	}
	
	///////////////////////////////////////////////////////////////////////
	
	//보드에 레인 생성
	@PostMapping("/{boardNo}")
	public void create(@PathVariable long boardNo, @RequestBody LaneDto laneDto) {
		laneDto.setBoardNo(boardNo);
		laneDao.createLane(laneDto);
		boardService.sendMessage(boardNo);
	}
	
	//lane order 변경
	@PutMapping("/{boardNo}/order")
	public void updateOrder(@PathVariable long boardNo, @RequestBody List<LaneDto> laneDtoList) {
		laneDao.updateOrderAll(laneDtoList);
		boardService.sendMessage(boardNo);
	}
	
	//no로 레인 삭제
	@DeleteMapping("/{laneNo}")
	public void delete(@PathVariable long laneNo) {
		LaneDto findDto = laneDao.selectOne(laneNo);
		if(findDto == null) throw new TargetNotFoundException("대상 없음");
		
		long boardNo = findDto.getBoardNo();
		laneDao.deleteLane(laneNo);
		
		//보드의 나머지 레인 order 변경
		List<LaneDto> lanes = laneDao.selectLaneList(boardNo);
		if(lanes.size() <= 0) {
			boardService.sendMessage(boardNo);
			return;
		}
		
		for (int i = 0; i < lanes.size(); i++) {
		    lanes.get(i).setLaneOrder(i + 1);
		}
		laneDao.updateOrderAll(lanes);
		boardService.sendMessage(boardNo);
	}

}
