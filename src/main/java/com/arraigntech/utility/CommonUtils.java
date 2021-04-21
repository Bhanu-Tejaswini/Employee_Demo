package com.arraigntech.utility;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.arraigntech.Exception.AppException;

/**
 * 
 * @author tulabandula.kumar
 *
 */
@Component
public class CommonUtils {
	
	public static LoggedInUserDetails getUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof LoggedInUserDetails)
			return (LoggedInUserDetails) principal;
		throw new AppException("User not found!!");
	}

}
