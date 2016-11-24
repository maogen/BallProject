package com.zgmao.ball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(value = { "com.zgmao.controller" })
public class BallServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BallServiceApplication.class, args);
	}
}
