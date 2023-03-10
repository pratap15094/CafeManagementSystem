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
import com.cafe.dao.ProductDao;
import com.cafe.jwt.JwtFilter;
import com.cafe.pojo.Category;
import com.cafe.pojo.Product;
import com.cafe.utils.CafeUtils;
import com.cafe.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin()) {
				if(validateProductMap(requestMap, false)) {
					productDao.save(getProductFromMap(requestMap, false));
					return CafeUtils.getResponseEntity("Product added succssfully",HttpStatus.OK );
				}
			return CafeUtils.getResponseEntity(CafeContants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
			else {
				return CafeUtils.getResponseEntity(CafeContants.UNAAUTORIZE_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} 
		catch(Exception ex) {
			ex.printStackTrace();
		}
	return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);	
	}

	
	

	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
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
	
	
	private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
		Category catgory = new Category();
		catgory.setId(Integer.parseInt(requestMap.get("categoryId")));
		Product product =  new Product();
		if(isAdd) {
			product.setId(Integer.parseInt(requestMap.get("id")));
		}else {
			product.setStatus("true");
		}
		
		product.setCategory(catgory);
		product.setName(requestMap.get("name"));
		product.setDescription(requestMap.get("description"));
		product.setPrice(Integer.parseInt(requestMap.get("price")));
		return product;
	}




	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		try {
			return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}




	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin()) {
				if(validateProductMap(requestMap, true)) {
					Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
					if(!optional.isEmpty()) {
						Product product = getProductFromMap(requestMap, true);
						product.setStatus(optional.get().getStatus());
						productDao.save(product);
						return CafeUtils.getResponseEntity("Product updated successfully",HttpStatus.OK );
					}else {
						return CafeUtils.getResponseEntity("Product id does't exist",HttpStatus.OK );
					}
					
				}else {
					return CafeUtils.getResponseEntity(CafeContants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}
			}else {
				return CafeUtils.getResponseEntity(CafeContants.UNAAUTORIZE_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}




	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		try {
			if(jwtFilter.isAdmin()) {
				Optional optional = productDao.findById(id);
				if(!optional.isEmpty()) {
					productDao.deleteById(id);
					return CafeUtils.getResponseEntity("Product deleted successfully", HttpStatus.OK);
				}else {
					return CafeUtils.getResponseEntity("Product id does not exist", HttpStatus.OK);
				}
			}else {
				return CafeUtils.getResponseEntity(CafeContants.UNAAUTORIZE_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}




	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin()) {
				Optional optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
				if(!optional.isEmpty()) {
					productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					return CafeUtils.getResponseEntity("Product status updated successfully", HttpStatus.OK);
				}
				else 
					return CafeUtils.getResponseEntity("Product id does not exist", HttpStatus.OK);
			}
			else
			  return CafeUtils.getResponseEntity(CafeContants.UNAAUTORIZE_ACCESS, HttpStatus.UNAUTHORIZED);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}




	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}




	@Override
	public ResponseEntity<ProductWrapper> getProductById(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	

}
