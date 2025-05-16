package com.kh.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CardFullDto {
	private long cardNo;
	private String cardTitle;
	private int cardOrder;
	private long laneNo;
	private String cardColor;
	private String cardComplete;
	private Long cardPic;//Person in charge 담당자라는 뜻임, null 가능
	
	private long accountNo;
	private String accountNickname;
	private String accountEmail;
}
