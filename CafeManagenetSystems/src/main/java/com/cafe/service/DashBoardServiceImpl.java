package com.cafe.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.dao.BillDao;
import com.cafe.dao.CategoryDao;
import com.cafe.dao.ProductDao;
import com.cafe.jwt.JwtFilter;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private BillDao billDao;
	
	@Autowired
	private ProductDao productlDao;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		Map<String, Object> map = new HashMap<>();
		map.put("category", categoryDao.count());
		map.put("product", productlDao.count());
		map.put("bill", billDao.count());
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

}
