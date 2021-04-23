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
	
	public static String getUser() {
			return SecurityContextHolder.getContext().getAuthentication().getName();
		}

}
