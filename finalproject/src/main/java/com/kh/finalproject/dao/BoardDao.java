package com.kh.finalproject.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.BoardDto;

@Repository
public class BoardDao {

	@Autowired
	private SqlSession sqlSession;
	
	public long createBoard(BoardDto boardDto) {
		long boardNo = sqlSession.selectOne("board.sequence");
		boardDto.setBoardNo(boardNo);
		sqlSession.insert("board.createBoard", boardDto);
		return boardNo;
	}
	
	public List<BoardDto> selectBoardList(long accountNo) {
		return sqlSession.selectList("board.selectBoardList", accountNo);
	}

	public BoardDto selectOne(long boardNo) {
		return sqlSession.selectOne("board.selectOne", boardNo);
	}
}
