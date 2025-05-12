package com.kh.finalproject.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebSocketEventHandler {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;//전송 도구
	
	//접속한 사용자들을 관리하기 위한 저장소를 생성
	//private List<UserVO> users = new CopyOnWriteArrayList<>();//순서유지
	
	@EventListener
	public void whenUserEnter(SessionConnectEvent event) {
		//헤더 정보를 추출
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();//웹소켓의 세션 ID(같은 사용자라도 기기가 다르면 다르게 나옴)
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		log.debug("사용자 접속");
		
		if(accessToken == null) return; //비회원 차단
	}
	
	@EventListener
	public void whenUserSubscribe(SessionSubscribeEvent event) {
		log.debug("채널 구독 완료");
	}
	
	@EventListener
	public void whenUserLeave(SessionDisconnectEvent event) {
		log.debug("사용자 접속 종료");
	}
}
