package com.kh.finalproject.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.kh.finalproject.websocket.UserInfoVO;

@Component
public class BoardUserComponent {
    // 보드번호와 유저리스트
	private Map<Long, Set<UserInfoVO>> nowUsers = Collections.synchronizedMap(new HashMap<>());
	//구독아이디와 보드번호 저장
	private Map<String, Long> subToBoardMap = Collections.synchronizedMap(new HashMap<>());
	//세션과 구독아이디 저장
	private Map<String, Set<String>> sessionToSubMap = Collections.synchronizedMap(new HashMap<>());
	
    //유저 추가
    public void addUser(long boardNo, UserInfoVO userInfoVO) {
        //만약 이 보드에 아무도 없으면 새로운 리스트 만들기
        if (!nowUsers.containsKey(boardNo)) {
        	nowUsers.put(boardNo, new HashSet<>());
        }
        // 유저 추가 (중복 자동 방지)
        nowUsers.get(boardNo).add(userInfoVO);
    }
    //유저 제거, subscriptionId를 가지고 제거
    public void removeUser(long boardNo, String subscriptionId) {
        Set<UserInfoVO> users = nowUsers.get(boardNo);
        if (users != null) {
            users.removeIf(user -> user.getSubscriptionId().equals(subscriptionId));
            // 아무도 없으면 방 삭제
            if (users.isEmpty()) {
                nowUsers.remove(boardNo);
            }
        }
    }
    //유저 리스트 가져오기, 보드번호를 가지고
    public Set<UserInfoVO> getUsers(long boardNo) {
        if (nowUsers.containsKey(boardNo)) {
            return nowUsers.get(boardNo);
        } else {
            return new HashSet<>();
        }
    }

    
    //구독아이디-보드 추가
    public void addSubToBoard(String subscriptionId, long boardNo) {
    	subToBoardMap.put(subscriptionId, boardNo);
    }
    //구독아이디-보드 에서 구독아이디 가지고 보드 번호 조회
    public long getBoardBySub(String subscriptionId) {
    	return subToBoardMap.getOrDefault(subscriptionId, -1L);
    }
    //구독아이디-보드에서 구독아이디 가지고 제거하기
    public void removeSubToBoard(String subscriptionId) {
    	subToBoardMap.remove(subscriptionId);
    }
    
    
    //세션-구독아이디 추가
    public void addSessionToSub(String sessionId, String subId) {
    	sessionToSubMap.computeIfAbsent(sessionId, k -> new HashSet<>()).add(subId);
    }
    //세션-구독에서 세션아이디로 구독아이디 조회
    public Set<String> getSubsBySession(String sessionId) {
        return sessionToSubMap.getOrDefault(sessionId, Collections.emptySet());
    }
    //세션-구독에서 세션아이디로 삭제하기
    public void removeSessionToSub(String sessionId, String subId) {
        Set<String> subs = sessionToSubMap.get(sessionId);
        if (subs != null) {
            subs.remove(subId);
            if (subs.isEmpty()) {
                sessionToSubMap.remove(sessionId);
            }
        }
    }


    // 세션 자체 제거 (모든 구독 clean)
    public void removeAllSubsBySession(String sessionId) {
        sessionToSubMap.remove(sessionId);
    }

}

