package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.AdminVO;
import com.demo.mapper.AdminMapper;

import lombok.Setter;

@Service
public class AdminServiceImpl implements AdminService{

	//어드민 매퍼 주입
	@Setter(onMethod_ = {@Autowired})
	private AdminMapper adminMapper;

	//로그인
	@Override
	public AdminVO admin_ok(String admin_id) {
		
		return adminMapper.admin_ok(admin_id);
	}

	//로그인 시간 업데이트
	@Override
	public void loginUpdate(String admin_id) {
		adminMapper.loginUpdate(admin_id);
		
	}
}
