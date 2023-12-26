package org.example.tgpocodabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TgPagodaBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgPagodaBotApplication.class, args);
	}

}
