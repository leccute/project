package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.CategoryVO;
import com.demo.domain.ProductVO;
import com.demo.dto.Criteria;
import com.demo.mapper.AdProductMapper;

import lombok.Setter;

@Service
public class AdProductServiceImpl implements AdProductService{

	//매퍼 주입
	@Setter(onMethod_ = {@Autowired})
	private AdProductMapper adProductMapper;

	//1차 카테고리 목록 가져오기
	@Override
	public List<CategoryVO> getCategoryList() {
		
		return adProductMapper.getCategoryList();
	}

	//1차 카테고리 참조 2차 카테고리 목록
	@Override
	public List<CategoryVO> getSubCategoryList(Integer cate_code) {
		
		return adProductMapper.getSubCategoryList(cate_code);
	}

	//상품 정보 저장
	@Override
	public void productInsert(ProductVO vo) {
		
		adProductMapper.productInsert(vo);
	}

	//상품 목록
	@Override
	public List<ProductVO> getProductList(Criteria cri) {
		
		return adProductMapper.getProductList(cri);
	}

	//상품 갯수
	@Override
	public int getProductCount(Criteria cri) {
		
		return adProductMapper.getProductCount(cri);
	}

	//수정할 상품 정보
	@Override
	public ProductVO getProductByNum(Integer pdt_num) {
		
		return adProductMapper.getProductByNum(pdt_num);
	}
	
	//상품 수정
	@Override
	public void productModify(ProductVO vo) {
		adProductMapper.productModify(vo);
		
	}

	

}
