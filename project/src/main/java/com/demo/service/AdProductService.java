package com.demo.service;

import java.util.List;

import com.demo.domain.CategoryVO;
import com.demo.domain.ProductVO;
import com.demo.dto.Criteria;

public interface AdProductService {

	//1차 카테고리 목록 가져오기
	List<CategoryVO> getCategoryList();
	
	//1차 카테고리 참조 2차 카테고리 목록
	List<CategoryVO> getSubCategoryList(Integer cate_code);
	
	//상품 정보 저장
	void productInsert(ProductVO vo);
	
	//상품 목록
	List<ProductVO> getProductList(Criteria cri);
	
	//상품 갯수
	int getProductCount(Criteria cri);
	
	//수정할 상품 정보
	ProductVO getProductByNum(Integer pdt_num);
	
	//상품 수정
	void productModify(ProductVO vo);
}
