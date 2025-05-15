package com.kh.finalproject.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//보드에서 공개될 유저의 정보
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = {"accountNo"})
public class UserInfoVO {
	private long accountNo;
	private String accountNickname;
	private String accountEmail;
	private String subscriptionId; //구독 id
	private String sessionId; //세션 id
}
