package com.kh.finalproject.configuration;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "custom.token")
public class TokenProperties {
	private String issuer;//issuer
	private String secretKey;//secret-key
	private int accessLimit;//access-limit
	private int refreshLimit;//refresh-limit
	private int renewalLimit;//renewal-limit
	
	//key 반환하는 메소드
	public SecretKey getKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}
}
