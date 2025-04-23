package com.kh.finalproject.dto;

import java.util.List;

import lombok.Data;

@Data
public class CardColumnDto {
	private ColumnDto columnDto;
	private List<CardDto> cards;
}
