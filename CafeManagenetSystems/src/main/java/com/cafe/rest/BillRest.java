package com.cafe.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cafe.pojo.Bill;

@RequestMapping("/bill")
public interface BillRest {
	
	@PostMapping("/generateReport")
	ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap);

	@GetMapping("/getBills")
	ResponseEntity<List<Bill>> getBills();
	
	@PostMapping("/getPdf")
	ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap);

	@DeleteMapping("/deleteBill/{id}")
	ResponseEntity<String> deleteBill(@PathVariable Integer id);
}
