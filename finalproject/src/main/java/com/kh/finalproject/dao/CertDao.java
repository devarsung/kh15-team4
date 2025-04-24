package com.kh.finalproject.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.CertDto;

@Repository
public class CertDao {

	@Autowired
	private SqlSession sqlSession;
	
	public void insert(CertDto certDto) {
		sqlSession.insert("cert.add", certDto);
	}
	
	public boolean update(CertDto certDto) {
		//인증번호가 이미 있는경우 한번더 부르면 얘가 실행됨, 덮어쓰기용
		return sqlSession.update("cert.update", certDto) > 0;
	}
	
	public CertDto selectOne(String certEmail) {
		return sqlSession.selectOne("cert.find", certEmail);
	}
	
	public CertDto selectOne(CertDto certDto) {
		return sqlSession.selectOne("cert.find", certDto);
	}
	
	public void insertOrUpdate(CertDto certDto) {
		CertDto findDto = selectOne(certDto);
		if(findDto == null) {
			insert(certDto);
		}
		else {
			update(certDto);
		}
	}
	
	public boolean delete(String certEmail) {
		return sqlSession.delete("cert.delete", certEmail) > 0;
	}
	
	public boolean confirm(String certEmail) {
		return sqlSession.update("cert.confirm", certEmail) > 0;
	}
}
