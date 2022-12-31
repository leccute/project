package com.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.domain.CategoryVO;
import com.demo.domain.ProductVO;
import com.demo.dto.Criteria;
import com.demo.dto.PageDTO;
import com.demo.service.AdProductService;
import com.demo.util.FileUtils;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/admin/product/*")
public class AdProductController {

	//서비스 주입
	@Setter(onMethod_ = {@Autowired})
	private AdProductService adProductService;
	
	//사진 업로드 폴더(외부 폴더 경로) 주입
	@Resource(name = "uploadPath") //servlet-context.xml
	private String uploadPath;
	
	//ckeditor 폴더 주입
	@Resource(name = "uploadCkeditor")
	private String uploadCkeditor;
	
	//1차 카테고리 보이기
	@GetMapping("/productInsert")
	public void productInsert(Model model) {
		
		//productInsert.jsp 파일로 이동하고, 1차 카테고리 정보는 가져와서 뿌리기.
		
		List<CategoryVO> vo = adProductService.getCategoryList();
		
		model.addAttribute("categoryList", vo);
	}
	
	//1차 카테고리를 참조하는 2차 카테고리. ajax 사용. 사용하는 cate_code는 선택한 1차 카테고리의 value값.
	@ResponseBody
	@GetMapping("/getSubCategoryList/{cate_code}")
	public ResponseEntity<List<CategoryVO>> getSubCategoryList(@PathVariable("cate_code") Integer cate_code) {
		
		ResponseEntity<List<CategoryVO>> entity = 
				new ResponseEntity<List<CategoryVO>>(adProductService.getSubCategoryList(cate_code), HttpStatus.OK);
		
		return entity;
	}
	
	//CKEditor에서 사용하는 파일 업로드 기능. <input type="file" name="upload">
	@PostMapping("/imageUpload")
	public void imageUpload(HttpServletRequest req, HttpServletResponse res, MultipartFile upload) {
		
		OutputStream out = null; 
		PrintWriter printWriter = null; 
		
		res.setCharacterEncoding("utf-8"); 
		res.setContentType("text/html; charset=utf-8"); 
		
		try {
			String fileName = upload.getOriginalFilename(); //클라이언트에서 보낸 원본 파일 이름.
			byte[] bytes = upload.getBytes(); //업로드된 파일을 가리키는 byte 배열.
			
			//CKEditor를 통하여 업로드되는 서버측의 폴더
			//집 : String uploadPath = "C:\\study\\upload\\ckeditor\\" + fileName;
			//학원
			String uploadPath = uploadCkeditor + fileName;
			
			//1)출력스트림을 이용하여, 업로드 작업을 진행함.
			out = new FileOutputStream(new File(uploadPath));
			out.write(bytes);
			out.flush();
			//out.close();
			
			//2)업로드된 파일 정보를 CKEditor에 보내는 작업.
			printWriter = res.getWriter();
			String fileUrl = "/upload/" + fileName; //ckeditor에서 사용할 경로.
			
			//파일 정보를 json 포맷으로 준비해야 한다. 큰 따옴표를 내용으로 코딩할 경우에는 \"로 사용
			//{"filename":"abc.gif", "uploaded":1, "url":"/upload/abc.gif"} ckeditor 4.x version에서 요구하는 json 포맷
			printWriter.println("{\"filename\":\"" + fileName + "\", \"uploaded\":1, \"url\":\"" + fileUrl + "\"}");
			printWriter.flush();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(out != null) {
				try {
					out.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(printWriter != null) {
				printWriter.close();
			}
		}
	}
	
	//상품 정보 저장
	@PostMapping("/productInsert")
	public String productInsert(ProductVO vo, RedirectAttributes rttr) {
		
		log.info("vo: " + vo);
		
	//1)상품 이미지 파일 업로드 작업
		//날짜 폴더 생성하기 위한 날짜 형태의 문자열(운영 체제 경로의 구분자)
		String uploadDateFolderPath = FileUtils.getFolder();
		//파일 경로 및 파일 이름 생성, 서버에 이미지 업로드, 섬네일 이미지 생성.
		String saveImageName = FileUtils.uploadFile(uploadPath, uploadDateFolderPath, vo.getUploadFile());
		
		vo.setPdt_img(saveImageName); //DB에 저장될 업로드 파일명
		vo.setPdt_img_folder(uploadDateFolderPath); //날짜 폴더명
	
	//2)상품정보 저장.
		adProductService.productInsert(vo);
		
		//이동하는 주소의 jsp에서 참조.
		rttr.addFlashAttribute("msg", "상품 등록 성공");
		
		return "redirect:/admin/product/productList";
	}
	
	//상품 목록(페이징, 검색)
	@GetMapping("/productList")//@ModelAttribute("cri")클래스에서, Model(db에서) 둘 다 jsp에서 쓰기 위해. (이건가)
	public void ProductList(Criteria cri, Model model) { //@ModelAttribute 안 쓰고 해보기
		log.info(cri);
		
		List<ProductVO> productList = adProductService.getProductList(cri);
		
		//이미지 저장 폴더에 '\\'를 '/'로 고치기. 이래야 보인다.  
		productList.forEach(vo -> { //함수적 인터페이스(함수, 배열에 forEach 지원. for문 대체 가능)
			vo.setPdt_img_folder(vo.getPdt_img_folder().replace("\\", "/"));
		});
		
		model.addAttribute("productList", productList);
		
		//전체 데이터 갯수: 목록 작업에 필요.
		int productCount = adProductService.getProductCount(cri);
		model.addAttribute("pageMaker", new PageDTO(cri, productCount));
	}
	
	//상품 목록에서 이미지 보여주기
	@GetMapping("/displayFile")
	@ResponseBody //ajax 방식
	public ResponseEntity<byte[]> displayFile(String folderName, String fileName) throws Exception{
		
		// C:\\JAVA\\ezen_dev\\upload\\goods\\2022\\12\\20\\
		return FileUtils.getFile(uploadPath + folderName, fileName);
	}
	
	//상품 수정하기
	@GetMapping("/productModify")
	public void modify(Integer cate_code_prt, Integer cate_code, Integer pdt_num, Criteria cri, Model model) {
		
		//상품 등록 폼에 pdt_num=#{pdt_num}으로 저장된 정보(카테고리, 이미지, 상품 설명 등)가 들어 있는 상태로 불러오기.
		
		//1) 1차 카테고리 목록 작업
		List<CategoryVO> categoryList = adProductService.getCategoryList();
		model.addAttribute("cateList", categoryList);
		
		//2) 1차 참조 2차 카테고리 목록 작업
		List<CategoryVO> subCategoryList = adProductService.getSubCategoryList(cate_code_prt);
		model.addAttribute("subCateList", subCategoryList);
		
		//3)수정 상품 정보 불러오기
		ProductVO vo = adProductService.getProductByNum(pdt_num);
			//이미지 저장된 폴더명 바꾸기.
		vo.setPdt_img_folder(vo.getPdt_img_folder().replace("\\", "/"));
		model.addAttribute("productVO", vo);
	}
	
	//상품 수정하기
	@PostMapping("/productModify")
	public String productModify(ProductVO vo, RedirectAttributes rttr, Criteria cri) {
		
		//1)이미지 변경 업로드
		if(!vo.getUploadFile().isEmpty()) { //'파일폴더경로+파일이름'이라는 이름으로 업로드 폴더에 저장된 파일이 있으면
			//1-1)기존 이미지 삭제. 업로드패스 + 날짜폴더 + 상품 이미지
			FileUtils.deleteFile(uploadPath, vo.getPdt_img_folder(), vo.getPdt_img());
			
			//1-2)새 상품 이미지 업로드
			String uploadDateFolderPath = FileUtils.getFolder(); //날짜 폴더명 생성을 위한 날짜 데이터 작업.
					//폴더 생성 및 업로드
			String saveImageName = FileUtils.uploadFile(uploadPath, uploadDateFolderPath, vo.getUploadFile());
			
			//1-3) DB에 저장될 이미지 관련 정보 세팅
			vo.setPdt_img(saveImageName); //업로드 파일명
			vo.setPdt_img_folder(uploadDateFolderPath); //날짜 폴더명
		}
		
		//2)상품 수정
		adProductService.productModify(vo);
		
		rttr.addFlashAttribute("msg", "상품 수정 완료");
		
		return "redirect:/admin/product/productList" + cri.getListLink();
	}
	
	
	
}
