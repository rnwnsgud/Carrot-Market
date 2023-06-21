package com.guCoding.carrotMarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CarrotMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarrotMarketApplication.class, args);
	}

}
