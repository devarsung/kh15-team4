package com.kh.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LaneDto {
	private long laneNo;
	private String laneTitle;
	private int laneOrder;
	private long boardNo;
}
