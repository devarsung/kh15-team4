package com.kh.finalproject.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.AccountTokenDto;

@Repository
public class AccountTokenDao {
	@Autowired
	private SqlSession sqlSession;
	
	public AccountTokenDto insert(AccountTokenDto accountTokenDto) {
		long accountTokenNo = sqlSession.selectOne("accountToken.sequence");
		accountTokenDto.setAccountTokenNo(accountTokenNo);
		sqlSession.insert("accountToken.add", accountTokenDto);
		//return accountTokenDto;//현재 상태는 시간이 포함되지 않음
		return sqlSession.selectOne("accountToken.find", accountTokenDto);//모든 정보 반환
	}
	
	//target, value가 넘어온다
	public AccountTokenDto findByTargetAndToken(AccountTokenDto accountTokenDto) {
		return sqlSession.selectOne("accountToken.findByTargetAndToken", accountTokenDto);
	}
	
	public boolean delete(AccountTokenDto accountTokenDto) {
		return sqlSession.delete("accountToken.delete", accountTokenDto) > 0;
	}
	
	public boolean clean(Long accountTokenTarget) {
		return sqlSession.delete("accountToken.deleteByAccountTokenTarget", accountTokenTarget) > 0;
	}
	
	public boolean clean(AccountTokenDto accountTokenDto) {
		return sqlSession.delete("accountToken.deleteByAccountTokenTarget", accountTokenDto) > 0;
	}
}
