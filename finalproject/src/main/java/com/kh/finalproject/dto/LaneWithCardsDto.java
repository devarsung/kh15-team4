package com.kh.finalproject.dto;

import java.util.List;

import lombok.Data;

@Data
public class LaneWithCardsDto {
	private LaneDto laneDto;
	private List<CardDto> cardList;
}
