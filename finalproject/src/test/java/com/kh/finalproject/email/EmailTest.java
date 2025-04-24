package com.kh.finalproject.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalproject.service.CertService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class EmailTest {

	@Autowired
	private CertService certService;

	@Test
	public void test() {
		certService.sendCertEmail("devarsung@gmail.com");
	}
}
