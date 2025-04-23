package com.kh.finalproject.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.BoardDto;
import com.kh.finalproject.dto.CardColumnDto;

@Repository
public class BoardDao {

	@Autowired
	private SqlSession sqlSession;
	
	public long insert(BoardDto boardDto) {
		long boardNo = sqlSession.selectOne("board.sequence");
		boardDto.setBoardNo(boardNo);
		sqlSession.insert("board.add", boardDto);
		return boardNo;
	}
	
	public List<CardColumnDto> test() {
		return sqlSession.selectList("board.cardColumnList", 1);
	}
}
