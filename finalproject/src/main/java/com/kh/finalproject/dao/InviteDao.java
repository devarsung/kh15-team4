package com.kh.finalproject.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.BoardInviteDto;
import com.kh.finalproject.dto.InviteRejectDto;
import com.kh.finalproject.dto.InviteViewDto;

@Repository
public class InviteDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	//보드 초대장
	public void createBoardInvite(BoardInviteDto boardInviteDto) {
		long boardInviteNo = sqlSession.selectOne("invite.boardInviteSequence");
		boardInviteDto.setBoardInviteNo(boardInviteNo);
		sqlSession.insert("invite.createBoardInvite", boardInviteDto);
	}
	
	//유저의 초대장 목록
	public List<InviteViewDto> selectInviteViewList(BoardInviteDto boardInviteDto) {
		return sqlSession.selectList("invite.selectInviteViewList", boardInviteDto);
	}
	
	//유저의 초대장 목록 카운트
	public boolean selectInviteViewCount(BoardInviteDto boardInviteDto) {
		int count = sqlSession.selectOne("invite.selectInviteViewCount", boardInviteDto);
		return count > 0;
	}
	
	//읽지 않은 초대장이 있는가
	public int unreadInviteCount(long accountNo) {
		return sqlSession.selectOne("invite.unreadInviteCount", accountNo); 
	}
	
	//초대장 페이지 들어가면 초대장 전부 읽음으로 처리
	public void readInvite(long accountNo) {
		sqlSession.update("invite.readInvite", accountNo);
	}
	
	//수락하기
	public void acceptInvite(BoardInviteDto boardInviteDto) {
		sqlSession.update("invite.acceptInvite", boardInviteDto.getBoardInviteNo());
	}
	
	//거절하기
	public void rejectInvite(BoardInviteDto boardInviteDto) {
		sqlSession.update("invite.rejectInvite", boardInviteDto.getBoardInviteNo());
	}
	
	//거절 이력 생성
	public void createInviteReject(InviteRejectDto inviteRejectDto) {
		long sequence = sqlSession.selectOne("invite.rejectSequence");
		inviteRejectDto.setInviteRejectNo(sequence);
		sqlSession.insert("invite.createInviteReject", inviteRejectDto);
	}
	
	//이사람이 이보드를 거절한 적이 있는지 확인
	public boolean selectRejectHistory(long boardNo, long accountNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("boardNo", boardNo);
		params.put("accountNo", accountNo);
		int count = sqlSession.selectOne("invite.selectRejectHistory", params); 
		return count > 0;
	}
}
