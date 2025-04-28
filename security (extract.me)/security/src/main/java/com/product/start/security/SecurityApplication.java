package com.product.start.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		SpringApplication.run(SecurityApplication.class, args);
		System.out.println("Введите логин пользователя");


	}
}
