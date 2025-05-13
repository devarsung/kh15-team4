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

import com.kh.finalproject.dao.InviteDao;
import com.kh.finalproject.dto.BoardInviteDto;
import com.kh.finalproject.dto.InviteRejectDto;
import com.kh.finalproject.dto.InviteViewDto;
import com.kh.finalproject.service.TokenService;
import com.kh.finalproject.vo.ClaimVO;

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
	
	//보드 초대장 보내기
	@PostMapping("/")
	public void invite(@RequestHeader("Authorization") String accessToken, @RequestBody BoardInviteDto boardInviteDto) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = claimVO.getUserNo();
		boardInviteDto.setSenderNo(accountNo);
		inviteDao.createBoardInvite(boardInviteDto);
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
	
	@PatchMapping("/accept")
	public void acceptInvite(@RequestHeader("Authorization") String accessToken, @RequestBody BoardInviteDto boardInviteDto) {
		inviteDao.acceptInvite(boardInviteDto);
	}
	
	@PatchMapping("/reject")
	public void rejectInvite(@RequestHeader("Authorization") String accessToken, @RequestBody BoardInviteDto boardInviteDto) {
		System.out.println(boardInviteDto);
		//inviteDao.rejectInvite(boardInviteDto);
//		InviteRejectDto.builder().boardNo(0).accountNo(0).build();
//		inviteDao.createInviteReject(null);
	}

}
