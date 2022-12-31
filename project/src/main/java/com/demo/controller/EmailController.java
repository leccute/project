package com.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dto.EmailDTO;
import com.demo.service.EmailService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RequestMapping("/email/*")
@Log4j
@RestController //모두 ajax로 처리.
public class EmailController {

	//서비스 주입
	@Setter(onMethod_ = {@Autowired})
	EmailService emailService;
	
	//join.jsp에서 메일 인증 요청을 받았을 때, 인증코드 발송하기.
	@GetMapping("/send")
	public ResponseEntity<String> send(EmailDTO dto, HttpSession session){
		
		ResponseEntity<String> entity = null;
		
		//인증코드 6자리 생성
		String authCode = "";
		for(int i = 0; i < 6; i++) {
			authCode += String.valueOf((int)(Math.random()*10));
		}
		log.info("인증코드: " + authCode);
		
		//세션 형태로 인증코드 저장. (ajax로 계속 새로 고침이 되니까??)
		session.setAttribute("authCode", authCode);
		
		try {
			emailService.sendMail(dto, authCode);
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}
	
	//인증코드 입력 후, 확인버튼
	@PostMapping("/confirmAuthCode")
	public ResponseEntity<String> confirmAuthCode(String authCode, HttpSession session){
		ResponseEntity<String> entity = null;
		
		//세션에 저장되어 있는 인증코드 정보를 읽어온다.
		String confirm_authCode = (String)(session.getAttribute("authCode"));
		
		//세션 인증코드와 사용자 입력 인증코드를 비교한다.
		if(authCode.equals(confirm_authCode)) {
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
			
			//세션에 저장된 인증 코드는 더이상 사용하지 않으니 제거한다.
			session.removeAttribute("authCode");
		}else {
			entity = new ResponseEntity<String>("fail", HttpStatus.OK);
		}
		
		return entity;
	}
	
}
