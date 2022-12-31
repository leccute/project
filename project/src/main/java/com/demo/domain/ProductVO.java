package com.demo.domain;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductVO {

	private Integer pdt_num; //상품 번호
	private Integer cate_code; //2차
	private Integer cate_code_prt; //1차
	private String pdt_name; //상품명
	private Integer pdt_price; //상품가격
	private Integer pdt_discount; //상품 할인율
	
	private Integer pdt_dis_price; //할인이 적용된 상품 가격
	
	private String pdt_company; //상품 개발사
	private String pdt_detail; //상품 상세 소개
	
	private String pdt_img_folder; //이미지 저장 폴더명
	private String pdt_img;  //파일의 대표 이미지
	
	private Integer pdt_amount; //상품 남은 수량
	private String pdt_buy; //상품 구매 가능 여부
	private Date pdt_date_sub; //상품 등록날짜
	private Date pdt_date_up; //상품정보 수정날짜
	
	//파일 업로드 필드. (상품 이미지)
	//AdProductController의 /productInsert에 파라미터로 넣어도 되지만 여기저기 필요하니까 여기에 같이 만들었다.
	//<input type="file" class="form-control" id="uploadFile" name="uploadFile">
	private MultipartFile uploadFile;
}
