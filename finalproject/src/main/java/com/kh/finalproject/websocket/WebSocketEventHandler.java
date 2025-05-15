package com.kh.finalproject.websocket;

import java.util.Set;

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
		if(accessor.getDestination() == null) return;
		String sessionId = accessor.getSessionId();
		String subscriptionId = accessor.getSubscriptionId();
		
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
					.sessionId(sessionId)
				.build();
			
			//보드-사용자 맵에 추가
			boardUserComponent.addUser(boardNo, userInfoVO);
			//구독-보드 맵에 추가
			boardUserComponent.addSubToBoard(subscriptionId, boardNo);
			//세션-구독 맵에 추가
			boardUserComponent.addSessionToSub(sessionId, subscriptionId);
			System.out.println("세션:"+sessionId+", 구독:" + subscriptionId + "가 " + boardNo + "를 구독했다");
			
			//현재 접속중인 유저 전체에게 메세지 보내기
			messagingTemplate.convertAndSend("/private/users/" + boardNo, boardUserComponent.getUsers(boardNo));
		}
		
	}
	
	@EventListener
	public void whenUserUnsubscribe(SessionUnsubscribeEvent event) {
	    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
	    String sessionId = accessor.getSessionId();
	    String subscriptionId = accessor.getSubscriptionId();

	    //[1]구독번호를 가지고 boardNo 찾기 -> /private/users인 경우를 확인하는 것
	    long boardNo = boardUserComponent.getBoardBySub(subscriptionId);
	    if(boardNo != -1) {
	    	System.out.println("세션:"+sessionId+", 구독:" + subscriptionId + "가 " + boardNo + "를 구독취소했다");
	    	//보드-유저 제거
	    	boardUserComponent.removeUser(boardNo, subscriptionId);
	    	//구독-보드 제거
	    	boardUserComponent.removeSubToBoard(subscriptionId);
	    	//세션-구독 제거
	    	boardUserComponent.removeSessionToSub(sessionId, subscriptionId);;
	    	
	    	//유저 목록 업데이트 메세지
	    	messagingTemplate.convertAndSend("/private/users/" + boardNo, boardUserComponent.getUsers(boardNo));
	    }
	}

	@EventListener
	public void whenUserLeave(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
	    String sessionId = accessor.getSessionId();
	    System.out.println("세션 종료 : " + sessionId);
	    
	    //세션 아이디로 구독 아이디 전체 가져오기
	    Set<String> subscriptionIds = boardUserComponent.getSubsBySession(sessionId);
	    for(String subscriptionId : subscriptionIds) {
	    	long boardNo = boardUserComponent.getBoardBySub(subscriptionId);;
	    	if(boardNo != -1) {
	    		//보드-유저 제거
	    		 boardUserComponent.removeUser(boardNo, subscriptionId);
	    		//구독-보드 제거
	    		 boardUserComponent.removeSubToBoard(subscriptionId);
	    		//세션-보드 제거
	    		
	    		//유저목록 업데이트 후 메세지 전송
	    		 messagingTemplate.convertAndSend("/private/users/" + boardNo, boardUserComponent.getUsers(boardNo));
	    	}
	    }
	    
	    //세션 삭제
	    boardUserComponent.removeAllSubsBySession(sessionId);
	}

}
