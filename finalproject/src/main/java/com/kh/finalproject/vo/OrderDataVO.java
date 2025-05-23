package com.kh.finalproject.vo;

import java.util.List;

import com.kh.finalproject.dto.CardDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderDataVO {
	private List<CardDto> starting;
	private List<CardDto> arrival;
	private CardDto card;
}
