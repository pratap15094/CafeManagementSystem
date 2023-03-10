package com.cafe.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.cafe.pojo.Category;

public interface CategoryService {
	
	ResponseEntity<String> addNewCategory(Map<String, String> requestMap);
	
	ResponseEntity<List<Category>> getAllCategory(String filterValue);

	ResponseEntity<String> updateCategory(Map<String, String> requestMap);
	

}
