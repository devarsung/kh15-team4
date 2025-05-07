package com.kh.finalproject.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalproject.dao.CardDao;
import com.kh.finalproject.dto.CardDto;
import com.kh.finalproject.error.TargetNotFoundException;
import com.kh.finalproject.vo.OrderDataVO;

@CrossOrigin
@RestController
@RequestMapping("/api/card")
public class CardRestController {
	
	@Autowired
	private CardDao cardDao;
	
	//레인에 카드 생성
	@PostMapping("/{laneNo}")
	public void create(@PathVariable long laneNo, @RequestBody CardDto cardDto) {
		cardDto.setLaneNo(laneNo);
		cardDao.createCard(cardDto);
	}
	
	//card order 변경
	@PutMapping("/order")
	public void updateOrder(@RequestBody List<CardDto> cardDtoList) {
		cardDao.updateOrderAll(cardDtoList);
	}
	
	//card order 변경(레인 간)
	@PutMapping("/orderBetween")
	public void updateOrderBetween(@RequestBody OrderDataVO orderDataVO) {
		cardDao.updateLaneNo(orderDataVO.getCard());
		if(!orderDataVO.getStarting().isEmpty()) {
			cardDao.updateOrderAll(orderDataVO.getStarting());
		}
		if(!orderDataVO.getArrival().isEmpty()) {
			cardDao.updateOrderAll(orderDataVO.getArrival());
		}
	}
	
	//no로 카드 삭제
	@DeleteMapping("/{cardNo}")
	public void delete(@PathVariable long cardNo) {
		CardDto findDto = cardDao.selectOne(cardNo);
		if(findDto == null) throw new TargetNotFoundException("대상 없음");
		
		long laneNo = findDto.getLaneNo();
		cardDao.deleteCard(cardNo);
		
		//레인의 나머지 카드 order 변경
		List<CardDto> cards = cardDao.selectCardList(laneNo);
		if(cards.size() <= 0) return;
		
		for (int i = 0; i < cards.size(); i++) {
			cards.get(i).setCardOrder(i + 1);
		}
		cardDao.updateOrderAll(cards);
	}
}
