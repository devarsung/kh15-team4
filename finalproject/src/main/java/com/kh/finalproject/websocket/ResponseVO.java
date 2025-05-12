package com.kh.finalproject.websocket;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ResponseVO {
	private String content;
	private LocalDateTime time;
}
