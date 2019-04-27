package com.em;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class BdDemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(BdDemoApplication.class, args);
	}

}
