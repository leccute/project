package com.demo.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.domain.MemberVO;
import com.demo.dto.EmailDTO;
import com.demo.dto.LoginDTO;
import com.demo.service.EmailService;
import com.demo.service.MemberService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/member/*")
public class MemberController {

	//서비스 주입
	@Setter(onMethod_ = {@Autowired})
	MemberService memberService;
	
	//비밀번호 암호화 bean 주입 . spring-security.xml의 "bCryptPasswordEncoder" bean 주입을 받음.
	@Setter(onMethod_ = {@Autowired})
	private PasswordEncoder passwordEncoder;
	
	//email서비스 주입(인증코드 확인용 메일 관련)
	@Setter(onMethod_ = {@Autowired})
	private EmailService emailService;
	
	//회원가입폼
	@GetMapping("/join")
	public void join() {
		log.info("회원가입 폼");
	}
	
	// ID중복체크
	@ResponseBody  //ajax(비동기식)방식(회원가입 폼은 그대로, id 부분만 새로고침)
	@GetMapping("/idCheck")
	public ResponseEntity<String> idCheck(@RequestParam("mem_id") String mem_id){ //@RequestParam(요청 파라미터) 사용 이유?
		ResponseEntity<String> entity = null;
		
		String isUseID = "";
		if(memberService.idCheck(mem_id) != null) { //이미 있는 아이디
			isUseID = "no";
		}else { //사용 가능한 아이디
			isUseID = "yes";
		}
		
		entity = new ResponseEntity<String>(isUseID, HttpStatus.OK);
		
		return entity;
	}
	
	//회원 정보 저장
	@PostMapping("/join")
	public String join(MemberVO vo, RedirectAttributes rttr) { //RedirectAttributes 사용 이유? redirect되는 주소에서 사용해야 하나?
		
		//비밀번호 암호화
		String encodePW = passwordEncoder.encode(vo.getMem_pw());
		vo.setMem_pw(encodePW);
		
		//메일 수신 동의 관련 null 처리. (체크박스) 체크하지 않으면, 정보가 전송되지 않아 null처리 된다.
		//체크박스에 체크를 하면 "on"이 그 값이 된다.
		if(vo.getMem_accept_e() != null && vo.getMem_accept_e().equals("on")) {
			vo.setMem_accept_e("Y");
		}else {
			vo.setMem_accept_e("N");
		}
		
		memberService.join(vo);
		
		
		return "redirect:/member/login";
	}
	
	//로그인 폼 
	@GetMapping("/login")
	public void login() {
		log.info("로그인 폼");
	}
	
	//로그인 인증 (아이디 / 비밀번호 둘 다 제대로 입력하지 않으면, 로그인 되지 않는다.) //로그인 버튼을 눌렀을 때. id와 pw 정보가 LoginDTO 형태로 넘어온다.
	@PostMapping("/loginPost")
	public String loginPost(LoginDTO dto, HttpSession session, RedirectAttributes rttr) {
		//1)아이디가 불일치하는 경우 2)비밀번호가 불일치하는 경우 각각.
		MemberVO vo = memberService.login_ok(dto.getMem_id());
		
		String url = "";
		String msg = "";
		
		if(vo != null) {//일치하는 아이디 정보가 있음.
			String passwd = dto.getMem_pw(); //사용자 입력 비밀번호
			String db_passwd = vo.getMem_pw(); //db에서 가져 온 암호화된 비밀번호
			
			if(passwordEncoder.matches(passwd, db_passwd)) { //비밀번호 일치됨
				session.setAttribute("loginStatus", vo);
				
				url = "/";
				msg = "로그인이 되었습니다.";
			}else { //비밀번호 일치하지 않음
				url = "/member/login";
				msg = "비밀번호가 일치하지 않습니다.";
			}
		}else { // 아이디가 일치하지 않음
			url = "/member/login";
			msg = "아이디가 일치하지 않습니다.";
		}
		
		rttr.addFlashAttribute("msg", msg); //이동하는 주소에서 일시적으로 사용할 정보, 그 이름.
		
		return "redirect:" + url;
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes rttr) {
		
		session.invalidate(); //세션 소멸
		rttr.addFlashAttribute("msg", "로그아웃.");
		
		return "redirect:/";
	}
	
	//아이디, 비번찾기 폼
	@GetMapping("/lostpass")
	public void lostpass() {
		log.info("비밀번호 찾기 폼");
	}
	
	//아이디 찾기
	@PostMapping("/searchID")
	public String searchID(@RequestParam("mem_name") String mem_name, @RequestParam("mem_email") String mem_email, RedirectAttributes rttr) {
		
		String url = "";
		String msg = "";
		
		//1)저장된 이름과 이메일 주소가 있는지 확인해서 아이디 가져오기.
		String db_mem_id = memberService.getNameEmailExists(mem_name, mem_email);
		
		log.info(" 아이디 : " + db_mem_id);
		
		if(db_mem_id != null) { //데이터가 있어서 아이디가 있으면.
			//2) 아이디 정보 메일로 보내기
			EmailDTO dto = new EmailDTO("TheChan", "TheChan", mem_email, "TheChan 아이디입니다.", "아이디 : ");
			
			try {
				emailService.sendMail(dto, db_mem_id);
				url = "/member/login";
				msg = "아이디 정보가 메일로 발송되었습니다.";
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}else { //입력 정보가 없어서 가져 온 아이디가 없으면.
			url = "/member/lostpass";
			msg = "입력하신 정보가 일치하지 않습니다. 확인해주세요.";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
	}
	
	//비번찾기 : 임시비밀번호 발급
	@PostMapping("/searchPw")
	public String searchPw(@RequestParam("mem_id") String mem_id, @RequestParam("mem_email") String mem_email, RedirectAttributes rttr) { //@requestParam?
		
		String url = "";
		String msg = "";
		
		//1)저장된 아이디와 이메일 주소가 있는지 확인.
		String db_mem_id = memberService.getIDEmailExists(mem_id, mem_email);
		String temp_pw = "";
		
		if(db_mem_id != null) { //회원가입 된 정보
			//2)임시 비밀번호 생성
			UUID uid = UUID.randomUUID();
			log.info("임시 비밀번호: " + uid);
			
			temp_pw = uid.toString().substring(0, 6); //0번째부터 6개.
			
			//3)DB에 암호화한 임시 비밀번호 업데이트 하기
			memberService.changePW(db_mem_id, passwordEncoder.encode(temp_pw));
			
			//4)사용자에게 임시 비밀번호 메일 발송
			EmailDTO dto = new EmailDTO("TheChan", "TheChan", mem_email, "TheChan 임시 비밀번호입니다.", "임시 비밀번호 : ");
			
			try {
				emailService.sendMail(dto, temp_pw);
				url = "/member/login";
				msg = "메일이 발송되었습니다.";
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else { //회원가입 되지 않은 정보
			url = "/member/lostpass";
			msg = "입력하신 정보가 일치하지 않습니다. 확인해주세요.";
			
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
	}
	
	//회원정보 수정을 위한 비밀번호 재확인 폼
	@GetMapping("/confirmPW")
	public void confirmPW() {
		
	}
	
	//회원정보 수정을 위한 비밀번호 재확인
	@PostMapping("/confirmPW")
	public String confirmPW(@RequestParam("mem_pw") String mem_pw, HttpSession session, RedirectAttributes rttr) {
		String url = "";
		String msg = "";
		
		//로그인 상태에서 세션을 통하여, 사용자 아이디를 참조할 수 있다.
		String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		
		MemberVO vo = memberService.login_ok(mem_id);
		
		if(vo != null) {
			String db_passwd = vo.getMem_pw(); 
			
			if(passwordEncoder.matches(mem_pw, db_passwd)) { //비번 일치
				url = "/member/modify";
			}else { //비번 안일치
				url = "/member/confirmPW";
				msg = "비밀번호가 일치하지 않습니다.";
			}
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
	}
	
	//회원정보 수정 폼
	@GetMapping("/modify")
	public void modify(HttpSession session, Model model) {
		//아이디로 세션 정보 읽어오고, 세션에 저장된 회원 정보 읽어오고, 수정창에 정보 보이기
		String mem_id = ((MemberVO)(session.getAttribute("loginStatus"))).getMem_id();
		MemberVO vo = memberService.login_ok(mem_id);
		model.addAttribute("memberVO", vo);
	}
	
	//회원정보 수정
	@PostMapping("/modify")
	public String modify(HttpSession session, RedirectAttributes rttr, MemberVO vo) {

		//입력받은 비밀번호를 인코딩하고, setting하기
		String encodePW = passwordEncoder.encode(vo.getMem_pw());
		vo.setMem_pw(encodePW);
		
		//회원정보 업데이트
		memberService.modify(vo);
		
		rttr.addFlashAttribute("msg", "회원 정보 수정됨.");
		
		return "redirect:/";
	}
	
	
	
}
