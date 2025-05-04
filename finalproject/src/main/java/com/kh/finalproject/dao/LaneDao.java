package com.kh.finalproject.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.LaneDto;
import com.kh.finalproject.dto.LaneWithCardsDto;

@Repository
public class LaneDao {

	@Autowired
	private SqlSession sqlSession;
	
	public List<LaneDto> selectLaneList(long boardNo) {
		return sqlSession.selectList("lane.list", boardNo);
	}
	
	public void createLane(LaneDto laneDto) {
		int nextOrderNumber = sqlSession.selectOne("lane.nextOrderNumber", laneDto.getBoardNo());
		laneDto.setLaneOrder(nextOrderNumber);
		
		long laneNo = sqlSession.selectOne("lane.sequence");
		laneDto.setLaneNo(laneNo);
		sqlSession.insert("lane.add", laneDto);
	}
	
	public List<LaneWithCardsDto> selectLaneWithCards(long boardNo) {
		return sqlSession.selectList("lane.selectLaneWithCards", boardNo);
	}
	
	public boolean updateOrder(long laneNo, int laneOrder) {
		Map<String, Object> params = new HashMap<>();
		params.put("laneNo", laneNo);
		params.put("laneOrder", laneOrder);
		return sqlSession.update("lane.updateOrder", params) > 0;
	}

	public void updateOrderAll(List<LaneDto> laneDtoList) {
		Map<String, Object> params = new HashMap<>();
		params.put("laneDtoList", laneDtoList);
		sqlSession.update("lane.updateOrderAll", params);
	}
	
	//레인 지우기
	public boolean deleteLane(long laneNo) {
		return sqlSession.delete("lane.deleteLane", laneNo) > 0;
	}
	
	//no로 1개 찾기
	public LaneDto selectOne(long laneNo) {
		return sqlSession.selectOne("lane.find", laneNo);
	}
}
