package com.kh.finalproject.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AccountDto {
	private long accountNo;
	private String accountEmail;
	private String accountPw;
	private String accountNickname;
	private String accountTel;
	private Timestamp accountJoin;
}
