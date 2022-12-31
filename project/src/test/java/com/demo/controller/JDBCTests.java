package com.demo.controller;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 1)오라클 데이타베이스 드라이버를 이용한 연결테스트.
public class JDBCTests {

	private static final Logger logger = LoggerFactory.getLogger(JDBCTests.class);
	
	private String url = "jdbc:oracle:thin:@localhost:1521:xe"; // 로컬 컴퓨터의 오라클데이터베이스 서버를 가리키는 url
	private String uid = "zootopia";
	private String passwd = "1234";
	
	static {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		}catch(Exception ex) {
			ex.printStackTrace(); //콘솔에 예외정보를 출력하는 기능.
		}
	}
	
	//연결테스트 기능
	@Test // 아래메서드가 jUnit 단위테스트로 사용하는 의미.
	public void testConnection() {
		// AutoCloseable인터페이스를 상속받은 것만 try() 안에 사용할수가 있다. 그리고 자동으로 conn.close()를 내부적으로 호출한다.
		try(Connection conn = DriverManager.getConnection(url, uid, passwd)) {
			logger.info("연결성공");
		}catch(Exception ex) {
			fail(ex.getMessage());
		}
	}
	
	
	
}
