package com.demo.service;

import com.demo.dto.EmailDTO;

public interface EmailService {

	//회원가입 인증코드 보내기
	void sendMail(EmailDTO dto, String message);
}
