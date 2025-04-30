package com.kh.finalproject.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SingInResponseVO {
	private Long userNo;
	private String userEmail;
	private String accessToken;
	private String refreshToken;
}
