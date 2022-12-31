package com.demo.dto;

import lombok.Data;

@Data
public class PageDTO {

	// jsp에서  [이전]  1	 2	3	4	5	[다음] 형태를 만들기위한 정보를 구성하는 목적
	
	private int startPage; //각 블럭의 시작페이지 번호 저장 ex. 1, 6, 11
	private int endPage; //각 블럭의 마지막 페이지 번호 저장 ex. 5, 10, 15
	
	//이전, 다음 표시 여부
	private boolean prev;
	private boolean next;
	
	//총 데이터 개수
	private int total;
	
	//페이징과 검색 필드 (page, amount, type, keyword)
	private Criteria cri;
	
	//매개변수가 있는 생성자 정의
	public PageDTO(Criteria cri, int total) {
		this.cri = cri;
		this.total = total;
		
		//데이터 수
		int pageSize = 5; //블록에 표시될 페이지 개수
		int endPageInfo = pageSize - 1; //5 - 1 = 4
		
		//(현재 페이지수 / 총 페이지 사이즈) * 총 페이지 사이즈. ex. ceil((4/5)) * 5 = 5 -> endPage, 
		this.endPage = (int) (Math.ceil(cri.getPageNum() / (double) pageSize)) * pageSize;
		this.startPage = this.endPage - endPageInfo; // 5 - 4 = 1
		
		//실제 데이터 수를 이용한 전체 페이지 수. ex. ceil(100.0 / 5) = 20
		int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount()));
		
		if(realEnd <= this.endPage) {
			this.endPage = realEnd;
		}
		
		//이전, 다음 표시 여부
		this.prev = this.startPage > 1;  //첫 블록이 아니라 다음 블록부터 나와라
		this.next = this.endPage < realEnd; //최종 끝 페이지 이전의 마지막 페이지가 나와라
	}
	
}
