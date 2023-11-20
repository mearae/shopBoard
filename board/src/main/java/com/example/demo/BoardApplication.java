package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Gradle - Groovy, Java, 2.7.17
// Jar, 11
// Lombok, Spring Web, Thymeleaf, Spring Data JPA

// mysqltutorial.org -> start here -> 2. install mysql
//	-> http://dev.mysql.com/downloads/installer/ 클릭
//	-> 아래 걸로 다운로드 -> 로그인 없이 다운로드
// mysql workbench 실행 -> + 클릭 -> 이름, 비번 설정
//	-> 왼쪽에 schema 선택 후 오클릭 create schema(이름 green)
@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
