package com.portfolio.portfolio_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioAppApplication {

	
	public static void main(String[] args) {
		// to generate temperary secret key for JWT, uncomment the following lines and run the application once
		// byte[] key = new byte[32];
		// new java.security.SecureRandom().nextBytes(key);

		// System.out.println(java.util.Base64.getEncoder().encodeToString(key));

		SpringApplication.run(PortfolioAppApplication.class, args);
	}

}
