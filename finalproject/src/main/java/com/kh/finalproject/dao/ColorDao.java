package com.kh.finalproject.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.ColorDto;

@Repository
public class ColorDao {

	@Autowired
	private SqlSession sqlSession;
	
	public List<ColorDto> selectColorList() {
		return sqlSession.selectList("color.selectColorList");
	}
}
