package com.zgmao.ball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(value = { "com.zgmao.controller" })
@EnableJpaRepositories("com.zgmao.tableImpl")
@EntityScan("com.zgmao.table")
public class BallServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BallServiceApplication.class, args);
	}
}
