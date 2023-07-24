package com.atorres.nttdata.debitms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@EnableDiscoveryClient
@EnableReactiveFeignClients
@SpringBootApplication
public class DebitMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebitMsApplication.class, args);
	}

}
