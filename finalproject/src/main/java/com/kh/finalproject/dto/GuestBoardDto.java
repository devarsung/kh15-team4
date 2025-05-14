package com.kh.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class GuestBoardDto {
	private long boardNo;
	private String boardTitle;
	private long creatorNo;
	private String accountNickname;
}
