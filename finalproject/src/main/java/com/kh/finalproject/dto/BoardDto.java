package com.kh.finalproject.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BoardDto {
	private Long boardNo;
	private String boardTitle;
	private Long accountNo;
	private List<LaneDto> laneList;
}
