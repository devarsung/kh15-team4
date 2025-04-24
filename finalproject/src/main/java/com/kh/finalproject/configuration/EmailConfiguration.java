package com.kh.finalproject.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {

	@Autowired
	private EmailProperties emailProperties;
	
	@Bean
	public JavaMailSenderImpl sender() {
		//[1]도구생성
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		
		//[2]설정
		sender.setHost("smtp.gmail.com");//이용할 업체의 별칭 또는 IP(위치 정보)
		sender.setPort(587);//이용할 업체의 서비스 실행 번호
		sender.setUsername(emailProperties.getUsername());//이용할 업체의 사용자 계정이름
		sender.setPassword(emailProperties.getPassword());//이용할 업체의 사용자 계정 비밀번호(구글은 앱 비밀번호)
		
		Properties props = new Properties();//추가 정보 저장소 생성
		props.setProperty("mail.smtp.auth", "true");//인증 후 이용 설정
		props.setProperty("mail.smtp.debmg", "true");//디버깅 허용 설정
		props.setProperty("mail.smtp.starttls.enable", "true");//TLS 사용 설정
		props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");//TLS 버전 설정
		props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");//신뢰할 수 있는 주소로 등록
		sender.setJavaMailProperties(props);//추가 정보 설정
		
		return sender;
	}
}
