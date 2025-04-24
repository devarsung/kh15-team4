package com.kh.finalproject.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalproject.dao.BoardDao;
import com.kh.finalproject.dto.BoardDto;

@CrossOrigin
@RestController
@RequestMapping("/api/board")
public class BoardRestController {

	@Autowired
	private BoardDao boardDao;
	
	@PostMapping("/")
	public long create(@RequestBody BoardDto boardDto) {
		return boardDao.insert(boardDto);
	}
	
	@GetMapping("/{boardNo}")
	public BoardDto find(@PathVariable long boardNo) {
		return boardDao.selectOne(boardNo);
	}
}
