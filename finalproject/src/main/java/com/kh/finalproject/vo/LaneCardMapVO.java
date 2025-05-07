package com.kh.finalproject.vo;

import java.util.Map;

import com.kh.finalproject.dto.CardDto;
import com.kh.finalproject.dto.LaneDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LaneCardMapVO {
	private Map<String, LaneDto> laneMap;
	private Map<String, CardDto> cardMap;
}
