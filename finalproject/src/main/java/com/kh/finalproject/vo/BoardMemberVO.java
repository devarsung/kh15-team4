package com.kh.finalproject.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//보드에서 보여줄 멤버 정보
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BoardMemberVO {
	private long boardNo;
	private long accountNo;
	private String accountNickname;
	private String accountEmail;
}
