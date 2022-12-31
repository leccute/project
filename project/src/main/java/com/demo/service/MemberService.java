package com.demo.service;

import org.apache.ibatis.annotations.Param;

import com.demo.domain.MemberVO;

public interface MemberService {

	//아이디 중복 확인
	String idCheck(String mem_id);
	
	//회원가입 정보 저장
	void join(MemberVO vo);
	
	//로그인 인증
	MemberVO login_ok(String mem_id);
	
	//이름과 메일 정보 일치 여부
	String getNameEmailExists(@Param("mem_name") String mem_name, @Param("mem_email") String mem_email);
	
	//아이디와메일정보 일치여부
	String getIDEmailExists(String mem_id, String mem_email);
	
	//임시비번을 암호화하여 변경.
	void changePW(String mem_id, String enc_pw);
	
	//회원정보 수정
	void modify(MemberVO vo);
}
