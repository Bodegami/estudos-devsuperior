package com.devsuperior.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

//public class DemoApplication implements CommandLineRunner {

//	@Autowired
//	private PasswordEncoder passwordEncoder;

//	@Override
//	public void run(String... args) throws Exception {
//		System.out.println("ENCODE = " + passwordEncoder.encode("123456"));
//
//		boolean result = passwordEncoder.matches("123456", "$2a$10$VXUy89yz5qKoLCZYzoTnM.G9VxdTHn38ch74qWTNl.g0JLx4l5jRK");
//		System.out.println("MATCHES = " + result);
//	}
}
