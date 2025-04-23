package com.kh.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ColumnDto {
	private long columnNo;
	private String columnTitle;
	private int columnOrder;
	private long boardNo;
}
