package com.demo.dto;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

//페이징 정보 필드: 현재 페이지, 페이지마다 출력 데이터 건수
//검색 필드 : 검색 종류, 검색어

@Data
public class Criteria {

	//1)페이징 정보
	private int pageNum; //현재 페이지 번호
	private int amount; //게시물 출력 개수
	
	//2)검색 정보
	private String type; //검색 종류(분류 항목) ex. 상품명 - N, 제조사 - C, 상품명or제조사 - NC
	private String keyword; //입력한 검색어
	
	//기본 생성자 정의
	public Criteria() {
		this(1, 10); //매개 변수가 있는 생성자 메소드 호출
	}
	
	//매개변수가 있는 생성자
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum; //기본 1
		this.amount = amount; //기본 10
	}
	
	//검색 기능.xml mapper파일에서 사용될 메소드명. 메소드명의 규칙은  get이름() 형식이어야 한다.(getter메소드 형식)
	public String[] getTypeArr() {
		//검색항목 ex. 상품명 - N, 제조사 - C, 상품명or제조사 - NC
		return type == null? new String[] {} : type.split(""); //{"N", "C"}
	}
	
	//주소에 Criteria 클래스 파라미터 추가 작업 ?pageNum=값1&amount=값2&type=값3&keyword=값4
	public String getListLink() {
		
		//메소드 체이닝 문법
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
						.queryParam("pageNum", this.getPageNum())
						.queryParam("amount", this.getAmount())
						.queryParam("type", this.getType())
						.queryParam("keyword", this.getKeyword());
		
		return builder.toUriString();
	}
	
}
