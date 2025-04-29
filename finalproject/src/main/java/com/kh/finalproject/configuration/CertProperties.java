package com.kh.finalproject.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component//설정파일을 불러와서 전달하는 목적을 가지고 있다
@ConfigurationProperties(prefix = "custom.cert")
public class CertProperties {
	private int expireMinutes;//custom.cert.expire-minutes
	private int expireAccept;//custom.cert.expire-accept
}