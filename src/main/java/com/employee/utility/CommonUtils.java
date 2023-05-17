package com.employee.utility;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class CommonUtils {
	
	public static String getUser() {
			return SecurityContextHolder.getContext().getAuthentication().getName();
		}

}
