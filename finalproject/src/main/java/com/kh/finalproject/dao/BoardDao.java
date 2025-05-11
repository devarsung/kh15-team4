package com.kh.finalproject.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.BoardDto;
import com.kh.finalproject.dto.BoardInviteDto;
import com.kh.finalproject.dto.InviteViewDto;

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

	public boolean deleteBoard(long boardNo) {
		return sqlSession.delete("board.deleteBoard", boardNo) > 0;
	}
	
	public void enterBoard(long boardNo, long accountNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("boardNo", boardNo);
		params.put("accountNo", accountNo);
		sqlSession.insert("board.enterBoard", params);
	}
	
	public boolean selectBoardMember(long boardNo, long accountNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("boardNo", boardNo);
		params.put("accountNo", accountNo);
		int count = sqlSession.selectOne("board.selectBoardMember", params);
		return count > 0;
	}
	
	//보드 초대
	public void createBoardInvite(BoardInviteDto boardInviteDto) {
		long boardInviteNo = sqlSession.selectOne("board.boardInviteSequence");
		boardInviteDto.setBoardInviteNo(boardInviteNo);
		sqlSession.insert("board.createBoardInvite", boardInviteDto);
	}
	
	//초대장 목록
	public List<InviteViewDto> selectInviteViewList(long accountNo) {
		return sqlSession.selectList("board.selectInviteViewList", accountNo);
	}
}
