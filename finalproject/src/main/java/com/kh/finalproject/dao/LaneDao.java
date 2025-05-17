package com.kh.finalproject.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.LaneDto;
import com.kh.finalproject.dto.LaneFullDto;

@Repository
public class LaneDao {

	@Autowired
	private SqlSession sqlSession;
	
	public void createLane(LaneDto laneDto) {
		int nextOrder = sqlSession.selectOne("lane.selectNextOrder", laneDto.getBoardNo());
		laneDto.setLaneOrder(nextOrder);
		
		long laneNo = sqlSession.selectOne("lane.sequence");
		laneDto.setLaneNo(laneNo);
		sqlSession.insert("lane.createLane", laneDto);
	}
	
	//no로 1개 찾기
	public LaneDto selectOne(long laneNo) {
		return sqlSession.selectOne("lane.selectOne", laneNo);
	}
	
	public List<LaneDto> selectLaneList(long boardNo) {
		return sqlSession.selectList("lane.selectLaneList", boardNo);
	}
	
	public List<LaneFullDto> selectLaneFullList(long boardNo) {
		return sqlSession.selectList("lane.selectLaneFullList", boardNo);
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

	//제목 변경
	public void updateLaneTitle(long laneNo, LaneDto laneDto) {
		Map<String, Object> params = new HashMap<>();
		params.put("laneNo", laneNo);
		params.put("laneTitle", laneDto.getLaneTitle());
		sqlSession.update("lane.updateLaneTitle", params);
	}
	
}
