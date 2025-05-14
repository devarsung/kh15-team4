package com.kh.finalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dao.InviteDao;
import com.kh.finalproject.dto.BoardInviteDto;
import com.kh.finalproject.vo.InviteFromResponseVO;
import com.kh.finalproject.vo.InviteToResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class InviteService {
	
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private InviteDao inviteDao;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	public InviteFromResponseVO sendInvite(BoardInviteDto boardInviteDto) {
		InviteFromResponseVO fromResponse = new InviteFromResponseVO();
		
		//상대가 이미 보드의 멤버인지 확인
		boolean isMember = boardDao.selectBoardMember(boardInviteDto.getBoardNo(), boardInviteDto.getReceiverNo());
		if(isMember) {
			//log.debug("이미 보드멤버");
			fromResponse.setType("warning");
			fromResponse.setStatusMessage("이미 보드의 멤버입니다");
			return fromResponse;
		}
		//거절 이력 확인
		boolean isRejected = inviteDao.selectRejectHistory(boardInviteDto.getBoardNo(), boardInviteDto.getReceiverNo());
		if(isRejected) {
			//log.debug("거절 이력있음");
			fromResponse.setType("warning");
			fromResponse.setStatusMessage("거절한 사용자입니다");
			return fromResponse;
		}
		
		//PENDING 상태의 초대장이 있는지 확인
		boardInviteDto.setBoardInviteStatus("PENDING");
		boolean hasInvite = inviteDao.selectInviteViewCount(boardInviteDto);
		if(hasInvite) {
			//log.debug("대기중인 초대장이 있음");
			fromResponse.setType("warning");
			fromResponse.setStatusMessage("이미 초대신청을 보냈습니다");
			return fromResponse;
		}
		
		//메세지 전송
		InviteToResponseVO response = InviteToResponseVO.builder().hasInvitation(true).build();
		messagingTemplate.convertAndSend("/private/invite/" + boardInviteDto.getReceiverNo(), response);
		//디비 저장
		inviteDao.createBoardInvite(boardInviteDto);
		
		fromResponse.setType("success");
		fromResponse.setStatusMessage("초대신청을 보냈습니다");
		return fromResponse;
	}
}
