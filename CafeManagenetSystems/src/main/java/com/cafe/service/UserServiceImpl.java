package com.cafe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cafe.constants.CafeContants;
import com.cafe.dao.UserDao;
import com.cafe.jwt.CustomerUsersDetailsService;
import com.cafe.jwt.JwtFilter;
import com.cafe.jwt.JwtUtil;
import com.cafe.pojo.Users;
import com.cafe.utils.CafeUtils;
import com.cafe.utils.EmailUtils;
import com.cafe.wrapper.UserWrapper;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	
	private UserDao userDao;
	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private AuthenticationManager authanticatinManager;

	@Autowired
	private CustomerUsersDetailsService customerUsersDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private JwtFilter jwtFilter;

	// -----------------------------------------------------------------------
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			if (validateSignUpMap(requestMap)) {
				Users user = userDao.findByEmailId(requestMap.get("email"));
				if (Objects.isNull(user)) {
					userDao.save(getUserFromMap(requestMap));
					return CafeUtils.getResponseEntity("Successfully Registerd", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("Email already exits", HttpStatus.BAD_REQUEST);
				}
			} else {
				return CafeUtils.getResponseEntity(CafeContants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// ---------------------------------------------------------------------------------------------------
	private boolean validateSignUpMap(Map<String, String> requestMap) {
		if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email")
				&& requestMap.containsKey("password")) {
			return true;
		}
		return false;
	}

	// -----------------------------------------------------------------------------------------------------
	private Users getUserFromMap(Map<String, String> requestMap) {
		Users user = new Users();
		user.setName(requestMap.get("name"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setRole("user");
		user.setStatus("false");
		return user;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {
			Authentication auth = authanticatinManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

			if (auth.isAuthenticated()) {

				if (customerUsersDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String>(
							"{\"token\":\""
									+ jwtUtil.generateToken(customerUsersDetailsService.getUserDetails().getEmail(),
											customerUsersDetailsService.getUserDetails().getRole())
									+ "\"}",
							HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval." + "\"}",
							HttpStatus.BAD_REQUEST);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<String>("{\"message\":\"" + "Bad Credential." + "\"}", HttpStatus.BAD_REQUEST);
	}

	// ------------------------------------------------------------------------------------------
	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {

		try {
			if (jwtFilter.isAdmin()) {
				return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// -------------------------------------------
	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				java.util.Optional<Users> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
				if (!optional.isEmpty()) {
					userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					sendEmailToAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
					return CafeUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("User does not exist", HttpStatus.OK);
				}
			} else {
				return CafeUtils.getResponseEntity(CafeContants.UNAAUTORIZE_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// -----------------
	private void sendEmailToAdmin(String status, String user, List<String> allAdmin) {

		allAdmin.remove(jwtFilter.getCurrentUser());
		if (status != null && status.equalsIgnoreCase("true")) {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account approved",
					"USER:-" + user + "\n is approved by \n ADMIN" + jwtFilter.getCurrentUser() + ")", allAdmin);
		} else {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled",
					"USER:-" + user + "\n is disabled by \n ADMIN" + jwtFilter.getCurrentUser() + ")", allAdmin);
		}
	}

	@Override
	public ResponseEntity<String> checkToken() {
		return CafeUtils.getResponseEntity("true", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			Users userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
			if (!userObj.equals(null)) {
                   if(userObj.getPassword().equals(requestMap.get("oldPassword"))) {
                	  userObj.setPassword(requestMap.get("newPassword")); 
                	  userDao.save(userObj);
                	  return CafeUtils.getResponseEntity("password update successfully", HttpStatus.OK);
                   }
                   return CafeUtils.getResponseEntity("Incorrect old password", HttpStatus.BAD_REQUEST);
			}
			return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		try {
			Users user = userDao.findByEmail(requestMap.get("email"));
			if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
				emailUtils.forgotMail(user.getEmail(), "Credential by CafeMangementSystem", user.getPassword());
			}
		return CafeUtils.getResponseEntity("Check your mail for credentials", HttpStatus.OK);
		} 
		catch (Exception ex) {
		  ex.printStackTrace();
		}
	return CafeUtils.getResponseEntity(CafeContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
