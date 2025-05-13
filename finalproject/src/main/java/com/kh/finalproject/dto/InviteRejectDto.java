package com.kh.finalproject.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class InviteRejectDto {
	private long inviteRejectNo;
	private long boardNo;
	private long accountNo;
	private Timestamp rejectedAt;
}
