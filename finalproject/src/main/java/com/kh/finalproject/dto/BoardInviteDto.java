package com.kh.finalproject.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BoardInviteDto {
	private long boardInviteNo;
	private long boardNo;
	private long senderNo;
	private long receiverNo;
	private String boardInviteStatus;
	private String boardInviteRead;
	private Timestamp createdAt;
}
