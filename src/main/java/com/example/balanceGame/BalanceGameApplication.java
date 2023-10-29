package com.example.balanceGame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BalanceGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalanceGameApplication.class, args);
	}

}
