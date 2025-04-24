package com.kh.finalproject.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.LaneWithCardsDto;

@Repository
public class LaneDao {

	@Autowired
	private SqlSession sqlSession;
	
	public List<LaneWithCardsDto> findLaneWithCards(long boardNo) {
		return sqlSession.selectList("lane.findLaneWithCards", boardNo);
	}
}
