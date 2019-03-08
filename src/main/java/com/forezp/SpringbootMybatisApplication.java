package com.forezp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootMybatisApplication {

	public static void main(String[] args) {
		System.out.println("编码格式："+System.getProperty("file.encoding"));
		SpringApplication.run(SpringbootMybatisApplication.class, args);
	}
}
