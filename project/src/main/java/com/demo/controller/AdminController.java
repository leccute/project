package com.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.domain.AdminVO;
import com.demo.service.AdminService;

import lombok.Setter;

@Controller
@RequestMapping("/admin/*")
public class AdminController {

	//admin service 주입
	@Setter(onMethod_ = {@Autowired})
	private AdminService adminService;
	
	//비밀번호 암호화 bean 주입. spring-security.xml의 "bCryptPasswordEncoder" bean.
	@Setter(onMethod_ = {@Autowired})
	private PasswordEncoder passwordEncoder;
	
	//관리자 로그인 페이지
	@GetMapping("")
	public String adLogin() {
		return "/admin/adLogin";
	}
	
	//로그인 정보 확인
	@PostMapping("/admin_ok")
	private String admin_ok (AdminVO vo, HttpSession session, RedirectAttributes rttr) {
		
		AdminVO adminVO = adminService.admin_ok(vo.getAdmin_id());
		
		String url = "";
		String msg = "";
		
		if(adminVO != null) { //아이디가 있으면
			if(passwordEncoder.matches(vo.getAdmin_pw(), adminVO.getAdmin_pw())) { //비밀번호가 같으면
				
				//세션 정보 이름은 회원 세션 정보의 이름과 다르게 설정.
				session.setAttribute("adminStatus", adminVO);
				
				url = "/admin/admin_menu";
				msg = " 관리자 로그인 성공";
				
				//현재 접속(로그인)시간 업데이트 작업 : if문 이전에 하면, 실패한 로그인 정보가 뜰 수 있으니, 로그인 성공한 정보를 넣을 것.
				adminService.loginUpdate(vo.getAdmin_id());
				
			}else { //비밀번호 불일치
				url = "/admin/";
				msg = "비밀번호 불일치";
			}
		}else { //아이디 불일치
			url = "/admin/";
			msg = "아이디 불일치";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
	}
	
	//로그인 성공하면 볼 페이지 (관리 페이지 메인)
	@GetMapping("/admin_menu")
	public String admin_menu() {
		
		return "/admin/admin_menu";
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes rttr) {
		session.invalidate();
		rttr.addFlashAttribute("msg", "로그아웃 완료");
		
		return "redirect:/admin/";
	}
	
}
