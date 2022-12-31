package com.demo.dto;

import lombok.Data;

@Data
public class LoginDTO { //로그인 할 때 필요한 정보들.

	private String mem_id;
	private String mem_pw;
}
