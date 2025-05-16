package com.kh.finalproject.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.CardDto;
import com.kh.finalproject.dto.ColorDto;
import com.kh.finalproject.vo.ColumnValueVO;

@Repository
public class CardDao {

	@Autowired
	private SqlSession sqlSession;
	
	public void createCard(CardDto cardDto) {
		int nextOrder = sqlSession.selectOne("card.selectNextOrder", cardDto.getLaneNo());
		cardDto.setCardOrder(nextOrder);
		long cardNo = sqlSession.selectOne("card.sequence");
		cardDto.setCardNo(cardNo);
		sqlSession.insert("card.createCard", cardDto);
	}
	
	//no로 1개 찾기
	public CardDto selectOne(long cardNo) {
		return sqlSession.selectOne("card.selectOne", cardNo);
	}
	
	public List<CardDto> selectCardList(long laneNo) {
		return sqlSession.selectList("card.selectCardList", laneNo);
	}
	
	public void updateOrderAll(List<CardDto> cardDtoList) {
		Map<String, Object> params = new HashMap<>();
		params.put("cardDtoList", cardDtoList);
		sqlSession.update("card.updateOrderAll", params);
	}
	
	//laneNo 변경하기
	public void updateLaneNo(CardDto cardDto) {
		sqlSession.update("card.updateLaneNo", cardDto);
	}
	
	//카드 지우기
	public boolean deleteCard(long cardNo) {
		return sqlSession.delete("card.deleteCard", cardNo) > 0;
	}
	
	//카드 컬러 변경하기 - 안쓸예정
	public void updateCardColor(long cardNo, ColorDto colorDto) {
		Map<String, Object> params = new HashMap<>();
		params.put("cardNo", cardNo);
		params.put("colorCode", colorDto.getColorCode());
		sqlSession.update("card.updateCardColor", params);
	}
	
	//카드 부분 수정
	public boolean updateCard(long cardNo, ColumnValueVO vo) {
		Map<String, Object> params = new HashMap<>();
		params.put("cardNo", cardNo);
		params.put("column", vo.getColumn());
		params.put("value", vo.getValue());
		return sqlSession.update("card.updateCard", params) > 0;
	}
}
