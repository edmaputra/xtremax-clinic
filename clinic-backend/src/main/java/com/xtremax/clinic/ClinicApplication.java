package com.xtremax.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class ClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicApplication.class, args);
	}

	@PostConstruct
	public  void setDefaultTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Makassar"));
	}

}
