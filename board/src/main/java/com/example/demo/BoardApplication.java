package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Gradle - Groovy, Java, 2.7.17
// Jar, 11
// Lombok, Spring Web, Thymeleaf, Spring Data JPA

// mysqltutorial.org -> start here -> 2. install mysql
//	-> http://dev.mysql.com/downloads/installer/ 클릭
//	-> 아래 걸로 다운로드 -> 로그인 없이 다운로드


@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
