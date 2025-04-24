package com.kh.finalproject.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.kh.finalproject.dto.AccountDto;

@Repository
public class AccountDao {

	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public void insert(AccountDto accountDto) {
		//비밀번호 암호화
		String encrypt = encoder.encode(accountDto.getAccountPw());
		accountDto.setAccountPw(encrypt);
		
		//pk 추출
		long accountNo = sqlSession.selectOne("account.sequence");
		accountDto.setAccountNo(accountNo);
		
		sqlSession.insert("account.join", accountDto);
	}
	
	public AccountDto selectOne(long accountNo) {
		return sqlSession.selectOne("account.findByNo", accountNo);
	}
	
	public AccountDto selectOne(String accountEmail) {
		return sqlSession.selectOne("account.findByEmail", accountEmail);
	}
	
	public AccountDto login(AccountDto accountDto) {
		AccountDto findDto = selectOne(accountDto.getAccountEmail());
		if(findDto == null) return null;
		boolean isValid = encoder.matches(accountDto.getAccountPw(), findDto.getAccountPw());
		return isValid ? findDto : null;
	}
}
