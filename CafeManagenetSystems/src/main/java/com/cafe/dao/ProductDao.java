package com.cafe.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.cafe.pojo.Category;
import com.cafe.pojo.Product;
import com.cafe.wrapper.ProductWrapper;

public interface ProductDao extends JpaRepository<Product, Integer> {

	List<ProductWrapper> getAllProduct();

	@Modifying
	@Transactional
	Integer updateProductStatus(@Param("status") String string, @Param("id") int parseInt);

	List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

	ProductWrapper getProductById(@Param("id") Integer id);

}
