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
	
	//구독아이디와 보드no의 정보를 저장
	private Map<String, Long> subscriptionBoardMap = new HashMap<>();
	
    // 유저 추가
    public void addUser(long boardNo, UserInfoVO userInfoVO) {
        // 만약 이 보드에 아무도 없으면 새로운 리스트 만들기
        if (!nowUsers.containsKey(boardNo)) {
        	nowUsers.put(boardNo, new HashSet<>());
        }

        // 유저 추가 (중복 자동 방지)
        nowUsers.get(boardNo).add(userInfoVO);
    }

    // 유저 제거, subscriptionId를 가지고 제거
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

    // 유저 리스트 가져오기
    public Set<UserInfoVO> getUsers(long boardNo) {
        if (nowUsers.containsKey(boardNo)) {
            return nowUsers.get(boardNo);
        } else {
            return new HashSet<>();
        }
    }

    //구독-보드 추가
    public void addSubscriptionBoard(String subscriptionId, long boardNo) {
    	subscriptionBoardMap.put(subscriptionId, boardNo);
    }
    
    //구독-보드에서 보드번호 조회
    public long getBoardNoFromSubscriptionId(String subscriptionId) {
    	return subscriptionBoardMap.getOrDefault(subscriptionId, -1L);
    }
    
    //구독 해제시 지우기
    public void removeSubscription(String subscriptionId) {
    	subscriptionBoardMap.remove(subscriptionId);
    }
}

