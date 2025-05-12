package com.kh.finalproject.websocket;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.kh.finalproject.dto.BoardInviteDto;

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
	
	@MessageMapping("/invite")
	public void invite(Message<BoardInviteDto> message) {
		//정보 분석(header)
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		//String uuid = accessor.getFirstNativeHeader("uuid");
		
		//메세지 해석(body)
		BoardInviteDto body = message.getPayload();
		log.debug("body = {}", body);
		
		//[2]무슨 처리를 한다
//		ResponseVO response = ResponseVO.builder()
//				.content(content)
//				.time(LocalDateTime.now())
//			.build();
//		
//		//[3]수동으로 메세지를 보내자
//		//messagingTemplate.convertAndSend("채널명", 데이터);
//		messagingTemplate.convertAndSend("/public/chat", body);
	}
	
}
