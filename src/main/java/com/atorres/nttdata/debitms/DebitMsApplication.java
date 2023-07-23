package com.atorres.nttdata.debitms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DebitMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebitMsApplication.class, args);
	}

}
