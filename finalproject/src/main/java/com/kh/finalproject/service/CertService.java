package com.kh.finalproject.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kh.finalproject.configuration.CertProperties;
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
	@Autowired
	private CertProperties certProperties;
	
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
	
	public boolean checkCert(CertDto certDto) {
		CertDto findDto = certDao.selectOne(certDto);
		if(findDto == null) return false;//인증발송 내역 없음
		
		//조건1. DB인증번호와 제출한 번호가 같을 것
		boolean c1 = findDto.getCertNumber().equals(certDto.getCertNumber());
		
		//조건2. 제한시간을 넘기지 않을 것
		LocalDateTime t1 = findDto.getCertTime().toLocalDateTime();//발송시각
		LocalDateTime t2 = LocalDateTime.now();//현재시각
		Duration duration = Duration.between(t1, t2);//차이 계산
		boolean c2 = duration.toMinutes() < 10;//우선 하드코딩, 나중에 설정에서 빼와야 함
		boolean isValid = c1 && c2;//최종 결과
		
		if(isValid) {
			//삭제 or 인증시간 기록 둘 중 하나하기
			//certDao.delete(certDto.getCertEmail());//삭제
			certDao.confirm(certDto.getCertEmail());//인증시간 기록
			return true;//인증 성공
		}
		
		return false;//인증 실패
	}
	
	@Scheduled(cron = "0 0 * * * *")
	public void cleanCertData() {
		certDao.clean(certProperties.getExpireMinutes(), certProperties.getExpireAccept());
	}
}
