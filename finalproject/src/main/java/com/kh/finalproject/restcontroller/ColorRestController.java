package com.kh.finalproject.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalproject.dao.ColorDao;
import com.kh.finalproject.dto.ColorDto;

@CrossOrigin
@RestController
@RequestMapping("/api/color")
public class ColorRestController {
	
	@Autowired
	private ColorDao colorDao;
	
	@GetMapping("/")
	public List<ColorDto> list() {
		List<ColorDto> colorList = colorDao.selectColorList();
		return colorList;
	}
}
