package com.kh.finalproject.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker//STOMP 도우미 활성화
@Configuration//설정 파일임을 명시
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {//역할 명시: 웹소켓 도우미 설정파일

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//구독 가능한 채널의 접두사를 설정
		registry.enableSimpleBroker("/public", "/private");
		
		//사용자가 메세지를 보낼 수 있는 채널
		registry.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//웹소켓 연결 생성 주소(접속 주소), 구독이랑 다름, 연결이 먼저임
		registry.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")//허용 가능한 클라이언트 주소패턴(CORS와 비슷)
			.withSockJS();//ws를 http처럼 쓸 수 있게 하며 유용한 기능을 제공
	}
}
