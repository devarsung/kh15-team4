package com.kh.finalproject.websocket;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.kh.finalproject.dao.AccountDao;
import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dao.InviteDao;
import com.kh.finalproject.dto.AccountDto;
import com.kh.finalproject.dto.BoardInviteDto;
import com.kh.finalproject.service.TokenService;
import com.kh.finalproject.vo.ClaimVO;
import com.kh.finalproject.vo.InviteToResponseVO;

import lombok.extern.slf4j.Slf4j;

/*
 	웹소켓에서 메세지 수신 이휴의 작업을 처리하기 위한 컨트롤러
 	원래 웹소켓은 HTTP와 구조가 완전히 다르지만 브로커의 도움으로 비슷한 구조 구현
*/
@Slf4j
@Controller
public class WebSocketController {
	
	//더 복잡한 처리를 위해 수동을 메세지 전송해야한다 -> 도구 필요(SimpMessagingTemplate)
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private InviteDao inviteDao;
	
	@MessageMapping("/invite")
	public void invite(Message<BoardInviteDto> message) {
		//정보 분석(header)
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		if(accessToken == null || accessToken.startsWith("Bearer ") == false) return;
		
		//회원정보 조회
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		AccountDto accountDto = accountDao.selectOne(claimVO.getUserNo());
		if(accountDto == null) return;
		
		//메세지 해석(body)
		BoardInviteDto body = message.getPayload();
		
		//초대장을 보낸 상대가 이미 보드의 멤버인지 확인
		//거절 이력 확인
		//pending 상태의 초대장이 있는지 확인
		boolean isMember = boardDao.selectBoardMember(body.getBoardNo(), body.getReceiverNo());
		
		if(isMember == false) {
			body.setSenderNo(accountDto.getAccountNo());
			//초대장 보내기
			InviteToResponseVO response = InviteToResponseVO.builder().hasInvitation(true).build();
			messagingTemplate.convertAndSend("/private/invite/" + body.getReceiverNo(), response);
			
			//db 저장
			inviteDao.createBoardInvite(body);
		}
		else {
			log.debug("같은 사람");
		}
		
	}
	
}
