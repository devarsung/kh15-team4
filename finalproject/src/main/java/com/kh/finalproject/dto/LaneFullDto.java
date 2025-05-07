package com.kh.finalproject.dto;

import java.util.List;

import lombok.Data;

@Data
public class LaneFullDto {
	private LaneDto laneDto;
	private List<CardDto> cardList;
}
