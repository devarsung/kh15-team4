package com.kh.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CardDto {
	private Long cardNo;
	private String cardTitle;
	private int cardOrder;
	private Long laneNo;
}
