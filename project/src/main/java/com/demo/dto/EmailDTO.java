package com.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDTO {

	private String senderName;	//발신자 이름
	private String senderMail;	//발신자 메일주소
	private String receiverMail; //수신자 메일주소. 회원메일 주소로 사용
	private String subject;		//제목
	private String message;		//본문
	
	public EmailDTO() {
		this.senderName = "TheChan";
		this.senderMail = "TheChan";	//GMail SMTP 메일 서버 이용.
		this.subject = "TheChan 회원가입 메일 인증 코드입니다.";
		this.message = "인증 코드: ";
	}
}
