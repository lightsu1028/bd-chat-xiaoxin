package com.em;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.em.dao")
@ComponentScan
public class BdDemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(BdDemoApplication.class, args);
	}

}
