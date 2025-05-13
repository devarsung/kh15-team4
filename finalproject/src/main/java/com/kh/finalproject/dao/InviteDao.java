package com.kh.finalproject.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.BoardInviteDto;
import com.kh.finalproject.dto.InviteViewDto;

@Repository
public class InviteDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	//보드 초대
	public void createBoardInvite(BoardInviteDto boardInviteDto) {
		long boardInviteNo = sqlSession.selectOne("invite.boardInviteSequence");
		boardInviteDto.setBoardInviteNo(boardInviteNo);
		sqlSession.insert("invite.createBoardInvite", boardInviteDto);
	}
	
	//초대장 목록
	public List<InviteViewDto> selectInviteViewList(long accountNo) {
		return sqlSession.selectList("invite.selectInviteViewList", accountNo);
	}
	
	//읽지 않은 초대장이 있는가
	public int unreadInviteCount(long accountNo) {
		return sqlSession.selectOne("invite.unreadInviteCount", accountNo); 
	}
	
	//초대장 페이지 들어가면 초대장 전부 읽음으로 처리
	public void readInvite(long accountNo) {
		sqlSession.update("invite.readInvite", accountNo);
	}
}
