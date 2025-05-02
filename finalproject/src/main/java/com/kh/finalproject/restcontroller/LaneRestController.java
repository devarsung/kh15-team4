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
import com.kh.finalproject.error.TargetNotFoundException;
import com.kh.finalproject.vo.OrderDataVO;

@CrossOrigin
@RestController
@RequestMapping("/api/lane")
public class LaneRestController {
	
	@Autowired
	private LaneDao laneDao;
	
	//보드의 레인 리스트
	@GetMapping("/{boardNo}")
	public List<LaneDto> list(@PathVariable long boardNo) {
		return laneDao.selectLaneList(boardNo);
	}
	
	//보드에 레인 생성
	@PostMapping("/{boardNo}")
	public void create(@PathVariable long boardNo, @RequestBody LaneDto laneDto) {
		laneDto.setBoardNo(boardNo);
		laneDao.createLane(laneDto);
	}
	
	//lane order 변경
	@PutMapping("/order")
	public void updateOrder(@RequestBody List<OrderDataVO> orderDataList) {
		System.out.println(orderDataList);
		for(OrderDataVO vo: orderDataList) {
			laneDao.updateOrder(vo.getLaneNo(), vo.getLaneOrder());
		}
	}
	
	@DeleteMapping("/{laneNo}")
	public void delete(@PathVariable long laneNo) {
		LaneDto findDto = laneDao.selectOne(laneNo);
		if(findDto == null) throw new TargetNotFoundException("대상 없음");
		long boardNo = findDto.getBoardNo();
		
		laneDao.deleteLane(laneNo);
		
		List<LaneDto> lanes = laneDao.selectLaneList(boardNo);
		
		if(lanes.size() <= 0) return;
		
		for (int i = 0; i < lanes.size(); i++) {
		    lanes.get(i).setLaneOrder(i + 1);
		}
		laneDao.updateOrderAll(lanes);
	}
}
