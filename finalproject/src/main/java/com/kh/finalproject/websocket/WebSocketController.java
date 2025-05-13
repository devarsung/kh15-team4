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
	
	//메세지 수신을 좀 더 체계적인 형태은 Message<?>로 수신
	//- 장점: STOMP의 헤더와 바디를 각각 읽어서 처리할 수 있다(인증 등에 유리)
	//=> [1] 프론트에서 /app/chat ~ 으로 보내면
	@MessageMapping("/chat")
	public void chat(Message<ActionVO> message) {
		//정보 분석(header)
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String uuid = accessor.getFirstNativeHeader("uuid");
		
		//메세지 해석(body)
		ActionVO body = message.getPayload();
		
		//[2]무슨 처리를 한다
		String regex = "";
		String content = body.getContent();
		if(content.matches(regex)) {//조건 작동안함
			content = content.replaceAll(regex, "*****");
			//추가로 보낸사람에게 경고메세지 발송
			messagingTemplate.convertAndSend("/pravate/chat/" + uuid);
		}
		
		
		
		ResponseVO response = ResponseVO.builder()
				.content(content)
				.time(LocalDateTime.now())
			.build();
		
		//[3]수동으로 메세지를 보내자
		//messagingTemplate.convertAndSend("채널명", 데이터);
		messagingTemplate.convertAndSend("/public/chat", body);
	}
	
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
		boolean isPending = true;
		if(isMember == false) {
			body.setSenderNo(accountDto.getAccountNo());
			//초대장 보내기
			InviteResponseVO response = InviteResponseVO.builder().hasInvitation(true).build();
			messagingTemplate.convertAndSend("/private/invite/" + body.getReceiverNo(), response);
			
			//db 저장
			inviteDao.createBoardInvite(body);
		}
		else {
			log.debug("같은 사람");
		}
		
	}
	
}
