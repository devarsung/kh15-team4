package com.kh.finalproject.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.kh.finalproject.component.BoardUserComponent;
import com.kh.finalproject.dao.AccountDao;
import com.kh.finalproject.dto.AccountDto;
import com.kh.finalproject.service.SubscribeService;
import com.kh.finalproject.service.TokenService;
import com.kh.finalproject.vo.ClaimVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebSocketEventHandler {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;//전송 도구
	@Autowired
	private TokenService tokenService;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private SubscribeService subscribeService;
	@Autowired
	private BoardUserComponent boardUserComponent;
	
	@EventListener
	public void whenUserEnter(SessionConnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();//웹소켓의 세션 ID(같은 사용자라도 기기가 다르면 다르게 나옴)
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		//log.debug("사용자 접속");
		
		if(accessToken == null) return; //비회원 차단
	}
	
	@EventListener
	public void whenUserSubscribe(SessionSubscribeEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String subscriptionId = accessor.getSubscriptionId();
		if(accessor.getDestination() == null) return;
		
		if(accessor.getDestination().startsWith("/private/users/")) {
			int position = "/private/users/".length();
			String boardStr = accessor.getDestination().substring(position);
			long boardNo = Long.parseLong(boardStr);
			
			//사용자 정보 추출
			String accessToken = accessor.getFirstNativeHeader("accessToken");
			ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
			AccountDto accountDto = accountDao.selectOne(claimVO.getUserNo());
			
			UserInfoVO userInfoVO = UserInfoVO.builder()
					.accountNo(accountDto.getAccountNo())
					.accountNickname(accountDto.getAccountNickname())
					.accountEmail(accountDto.getAccountEmail())
					.subscriptionId(subscriptionId)
				.build();
			
			//현재보드, 현재 사용자정보 추가
			boardUserComponent.addUser(boardNo, userInfoVO);
			//구독아이디와 보드 정보 추가
			boardUserComponent.addSubscriptionBoard(subscriptionId, boardNo);
			
			log.debug("subscriptionId={}",subscriptionId);
			log.debug("board={}",boardNo);
			
			//현재 접속중인 유저 전체에게 메세지 보내기
			messagingTemplate.convertAndSend("/private/users/" + boardNo, boardUserComponent.getUsers(boardNo));
		}
		else if(accessor.getDestination().startsWith("/private/update/")) {
			int position = "/private/update/".length();
			String boardStr = accessor.getDestination().substring(position);
			long boardNo = Long.parseLong(boardStr);
			
			//사용자 정보 추출
			String accessToken = accessor.getFirstNativeHeader("accessToken");
			ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		}
	}
	
	@EventListener
	public void whenUserUnsubscribe(SessionUnsubscribeEvent event) {
	    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
	    String subscriptionId = accessor.getSubscriptionId();
	    //log.debug("subscriptionId={}",subscriptionId);
	    //구독번호를 가지고 boardNo 찾기
	    long boardNo = boardUserComponent.getBoardNoFromSubscriptionId(subscriptionId);
	    if(boardNo != -1) {
	    	//유저 제거
	    	boardUserComponent.removeUser(boardNo, subscriptionId);
	    	//유저 목록 업데이트 메세지
	    	messagingTemplate.convertAndSend("/private/users/" + boardNo, boardUserComponent.getUsers(boardNo));
	    }
	}

	
	@EventListener
	public void whenUserLeave(SessionDisconnectEvent event) {
		//log.debug("사용자 접속 종료");
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		//log.debug("나가는 방은 = {}", accessor.getDestination());
	}

}
