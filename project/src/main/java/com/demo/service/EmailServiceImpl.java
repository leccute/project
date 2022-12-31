package com.demo.service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.demo.dto.EmailDTO;

import lombok.Setter;

@Service
public class EmailServiceImpl implements EmailService{

	//mailSender bean 주입.
	@Setter(onMethod_ = {@Autowired})
	private JavaMailSender mailSender;
	
	
	//회원가입 인증코드 보내기
	@Override
	public void sendMail(EmailDTO dto, String message) { //dto에 받는 사람 주소 외에 다 구성되어 있다. message는 인증 코드를 의미한다.
		
		//메일 구성 정보를 담당하는 객체(받는 사람, 보내는 사람, 전자 우편 주소, 본문내용)
		MimeMessage msg = mailSender.createMimeMessage();
		
		try {
			//받는 사람 메일주소
			msg.addRecipient(RecipientType.TO, new InternetAddress(dto.getReceiverMail()));
			//보내는 사람(메일, 이름)
			msg.addFrom(new InternetAddress[] {new InternetAddress(dto.getSenderMail(), dto.getSenderName())});
			//메일 제목
			msg.setSubject(dto.getSubject(), "utf-8");
			//본문 제목
			msg.setText(message, "utf-8");
			
			mailSender.send(msg); //GMail SMTP서버를 이용하여, 메일 발송이 된다.
			
		}catch(Exception e) {
			e.printStackTrace();
		}
				
		
	}

}
