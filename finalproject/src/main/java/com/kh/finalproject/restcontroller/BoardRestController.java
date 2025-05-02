package com.kh.finalproject.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dto.BoardDto;
import com.kh.finalproject.service.TokenService;
import com.kh.finalproject.vo.ClaimVO;

@CrossOrigin
@RestController
@RequestMapping("/api/board")
public class BoardRestController {

	@Autowired
	private BoardDao boardDao;
	@Autowired
	private TokenService tokenService;
	
	//보드 생성
	@PostMapping("/")
	public long create(@RequestBody BoardDto boardDto, @RequestHeader("Authorization") String accessToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		boardDto.setAccountNo(((Long)claimVO.getUserNo()).longValue());
		return boardDao.insert(boardDto);
	}
	
	//특정 회원의 보드 리스트 조회
	@GetMapping("/")
	public List<BoardDto> list(@RequestHeader("Authorization") String accessToken) {
		ClaimVO claimVO = tokenService.parseBearerToken(accessToken);
		long accountNo = ((Long)claimVO.getUserNo()).longValue();
		return boardDao.selectList(accountNo);
	}
	
	@GetMapping("/{boardNo}")
	public BoardDto find(@PathVariable long boardNo) {
		return boardDao.selectOne(boardNo);
	}
}
