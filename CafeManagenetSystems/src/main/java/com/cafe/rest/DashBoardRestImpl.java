package com.cafe.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.service.DashBoardService;

@RestController
public class DashBoardRestImpl implements DashBoardRest {

	@Autowired
	DashBoardService dashBoardService;
	
	
	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		return dashBoardService.getCount();
	}

}
