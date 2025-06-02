package com.example.A_Task3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages ="com.example.A_Task3.client")
@SpringBootApplication
public class ATask3Application {

	public static void main(String[] args) {
		SpringApplication.run(ATask3Application.class, args);
	}

}
