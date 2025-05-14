package com.kh.finalproject.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dao.InviteDao;
import com.kh.finalproject.dto.BoardInviteDto;
import com.kh.finalproject.dto.InviteRejectDto;
import com.kh.finalproject.dto.InviteViewDto;
import com.kh.finalproject.error.TargetNotFoundException;
import com.kh.finalproject.service.InviteService;
import com.kh.finalproject.service.TokenService;
import com.kh.finalproject.vo.ClaimVO;
import com.kh.finalproject.vo.InviteFromResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/invite")
public class InviteRestController {
	
	@Autowired
	private TokenService tokenService;
	@Autowired
	private InviteDao inviteDao;
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private InviteService inviteService;
	
	//보드 초대장 보내기
	@PostMapping("/")
	public InviteFromResponseVO invite(@RequestHeader("Authorization") String accessToken, @RequestBody BoardInviteDto boardInviteDto) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = claimVO.getUserNo();
		boardInviteDto.setSenderNo(accountNo);
		InviteFromResponseVO result = inviteService.sendInvite(boardInviteDto);
		return result;
	}
	
	//초대장 리스트 화면
	@PostMapping("/list")
	public List<InviteViewDto> inviteList(@RequestHeader("Authorization") String accessToken, @RequestBody BoardInviteDto boardInviteDto) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = claimVO.getUserNo();
		boardInviteDto.setReceiverNo(accountNo);
		return inviteDao.selectInviteViewList(boardInviteDto);
	}
	
	//읽지 않은 초대장이 있는지 없는지
	@GetMapping("/unreadInviteCount")
	public int unreadInviteCount(@RequestHeader("Authorization") String accessToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = claimVO.getUserNo();
		return inviteDao.unreadInviteCount(accountNo);
	}
	
	//초대장 페이지 들어가면 전부 읽음으로 처리
	@GetMapping("/readInvite")
	public void readInvite(@RequestHeader("Authorization") String accessToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = claimVO.getUserNo();
		inviteDao.readInvite(accountNo);
	}
	
	//초대 수락하기
	@PatchMapping("/accept")
	public long acceptInvite(@RequestHeader("Authorization") String accessToken, @RequestBody BoardInviteDto boardInviteDto) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = claimVO.getUserNo();
		if(accountNo != boardInviteDto.getReceiverNo()) {
			throw new TargetNotFoundException("사용자가 다릅니다");
		}
		//초대장 -> ACCEPTED 로 변경
		inviteDao.acceptInvite(boardInviteDto);
		//보드의 멤버인지 확인 후 추가
		boolean isMember = boardDao.selectBoardMember(boardInviteDto.getBoardNo(), boardInviteDto.getReceiverNo());
		if(isMember) {
			throw new TargetNotFoundException("이미 멤버입니다.");
		} 
		
		boardDao.enterBoard(boardInviteDto.getBoardNo(), boardInviteDto.getReceiverNo());
		return boardInviteDto.getBoardNo();
	}
	
	//초대 거절하기
	@PatchMapping("/reject")
	public void rejectInvite(@RequestHeader("Authorization") String accessToken, @RequestBody BoardInviteDto boardInviteDto) {
		//초대장 -> reREJECTEDject로 변경
		inviteDao.rejectInvite(boardInviteDto);
		//거절 내역 생성하기
		InviteRejectDto inviteRejectDto = InviteRejectDto.builder().boardNo(boardInviteDto.getBoardNo()).accountNo(boardInviteDto.getReceiverNo()).build();
		inviteDao.createInviteReject(inviteRejectDto);
	}

}
