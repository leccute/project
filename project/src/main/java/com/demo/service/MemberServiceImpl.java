package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.MemberVO;
import com.demo.mapper.MemberMapper;

import lombok.Setter;

@Service
public class MemberServiceImpl implements MemberService{

	//매퍼 주입
	@Setter(onMethod_ = {@Autowired})
	MemberMapper memberMapper;

	//아이디 중복 확인
	@Override
	public String idCheck(String mem_id) {
		
		return memberMapper.idCheck(mem_id);
	}

	//회원가입 정보 저장
	@Override
	public void join(MemberVO vo) {
		
		 memberMapper.join(vo);
	}

	//로그인 인증
	@Override
	public MemberVO login_ok(String mem_id) {
		
		return memberMapper.login_ok(mem_id);
	}
	
	//이름과 메일 정보 일치 여부
	@Override
	public String getNameEmailExists(String mem_name, String mem_email) {
		
		return memberMapper.getNameEmailExists(mem_name, mem_email);
	}
		
	//아이디랑 이메일 정보 확인하기
	@Override
	public String getIDEmailExists(String mem_id, String mem_email) {
		
		return memberMapper.getIDEmailExists(mem_id, mem_email);
	}

	//임시 비밀번호 정보 저장하기
	@Override
	public void changePW(String mem_id, String enc_pw) {
		memberMapper.changePW(mem_id, enc_pw);
		
	}

	//회원 정보 수정
	@Override
	public void modify(MemberVO vo) {
		memberMapper.modify(vo);
		
	}
	
}
