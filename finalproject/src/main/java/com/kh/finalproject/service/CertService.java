package com.kh.finalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.kh.finalproject.dao.CertDao;
import com.kh.finalproject.dto.CertDto;
import com.kh.finalproject.util.RandomGenerator;

@Service
public class CertService {

	@Autowired
	private CertDao certDao;
	@Autowired
	private JavaMailSender sender;
	@Autowired
	private RandomGenerator randomGenerator;
	
	public void sendCertEmail(String email) {
		//인증번호 생성
		String number = randomGenerator.randomNumber(8);
		
		//메세지 생성
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("이메일의 제목");
		message.setText("인증번호는 [" + number + "] 입니다");
		sender.send(message);
		
		//DB 등록
		certDao.insertOrUpdate(
				CertDto.builder()
					.certEmail(email)
					.certNumber(number)
				.build()
		);
	}
}
