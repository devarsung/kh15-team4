package com.kh.finalproject.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class BCryptTest {

	@Test
	public void test() {
		//BCrypt는 암호화를 할 때마다 무작위 salt를 생성해서 결과가 달라지도록 처리하는 방식
		//암호화 결과는 60byte
		//- 4byte는 버전
		//- 3byte는 가중치
		//- 22byte는 salt
		//- 나머지(31byte)가 암호화된 결과
		
		//도구 생성
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		//암호화
		String origin = "hello";
		String encrypt = encoder.encode(origin);
		log.debug("origin={}", origin);
		log.debug("encrypt={}", encrypt);
		
		//비교 - encoder가 제공하는 명령으로 비교해야함
		String input = "hello";
		boolean isValid = encoder.matches(input, encrypt);
		
		String input2 = "world";
		boolean isValid2 = encoder.matches(input2, encrypt);
		
		log.debug("isValid={}", isValid);
		log.debug("isValid2={}", isValid2);
		
	}
}
