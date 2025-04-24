package com.kh.finalproject.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.BoardDto;

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

	public BoardDto selectOne(long boardNo) {
		return sqlSession.selectOne("board.find", boardNo);
	}
}
