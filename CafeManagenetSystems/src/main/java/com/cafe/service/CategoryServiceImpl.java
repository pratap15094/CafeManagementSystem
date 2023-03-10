package com.cafe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.constants.CafeContants;
import com.cafe.dao.CategoryDao;
import com.cafe.jwt.JwtFilter;
import com.cafe.jwt.JwtUtil;
import com.cafe.pojo.Category;
import com.cafe.utils.CafeUtils;
import com.google.common.base.Strings;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin()) {
				if(validateCategoryMap(requestMap, false)) {
					categoryDao.save(getCategoryFromMap(requestMap, false));
					return CafeUtils.getResponseEntity("Category added succssfully",HttpStatus.OK );
				}
			}else {
				return CafeUtils.getResponseEntity(CafeContants.UNAAUTORIZE_ACCESS, HttpStatus.UNAUTHORIZED);	
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);	
	}

	//---------------------------------------------------------------------------------------------
	private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
	 if(requestMap.containsKey("name")) {
		 if(requestMap.containsKey("id") && validateId) {
			 return true; 
		 }
		 else if(!validateId) {
			 return true;
		 }
	 }
	return false;
	}
	
	//---------------------------------------------------------------------------------------------
	private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdd) {
		Category category =  new Category();
		if(isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
		category.setName(requestMap.get("name"));
		return category;
	}

	
	//---------------------------------------------------------------------------------------------
	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
				return new ResponseEntity<List<Category>>(categoryDao.getAllCatgory(), HttpStatus.OK);
			}
		return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);	
		} 
		catch(Exception ex) {
			ex.printStackTrace();
		}
	return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	//-------------------------------------------------------------------------------------
	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateCategoryMap(requestMap, true)) {
					Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
					if (!optional.isEmpty()) {
						categoryDao.save(getCategoryFromMap(requestMap, true));
						return CafeUtils.getResponseEntity("Category updated successfully", HttpStatus.OK);
					} else {
						return CafeUtils.getResponseEntity("Category id does't exist", HttpStatus.OK);
					}
				}
				return CafeUtils.getResponseEntity(CafeContants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			} else {
				return CafeUtils.getResponseEntity(CafeContants.UNAAUTORIZE_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
