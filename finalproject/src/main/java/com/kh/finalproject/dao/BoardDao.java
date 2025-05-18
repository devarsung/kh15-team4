package com.kh.finalproject.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.BoardDto;
import com.kh.finalproject.dto.GuestBoardDto;
import com.kh.finalproject.vo.BoardMemberVO;

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
	
	public List<GuestBoardDto> selectGuestBoardList(long accountNo) {
		return sqlSession.selectList("board.selectGuestBoardList", accountNo);
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
	
	//보드 멤버인지 아닌지 확인
	public boolean selectBoardMember(long boardNo, long accountNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("boardNo", boardNo);
		params.put("accountNo", accountNo);
		int count = sqlSession.selectOne("board.selectBoardMember", params);
		return count > 0;
	}
	
	//보드의 멤버 전체
	public List<BoardMemberVO> selectBoardMemberList(long boardNo) {
		return sqlSession.selectList("board.selectBoardMemberList", boardNo);
	}
	
	//보드 제목 변경
	public void updateBoardTitle(long boardNo, BoardDto boardDto) {
		Map<String, Object> params = new HashMap<>();
		params.put("boardNo", boardNo);
		params.put("boardTitle", boardDto.getBoardTitle());
		sqlSession.update("board.updateBoardTitle", params);
	}
	
	//보드에서 나가기
	public void leaveBoard(long boardNo, long accountNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("boardNo", boardNo);
		params.put("accountNo", accountNo);
		sqlSession.delete("board.leaveBoard", params);
	}
}
