package com.arraigntech.utility;

import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.arraigntech.entity.User;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.repository.UserRespository;

/**
 * 
 * @author tulabandula.kumar
 *
 */
@Component
public class ResetUserDetails implements Action<User, Boolean> {
	
	public static final Logger log = LoggerFactory.getLogger(ResetUserDetails.class);
	
	@Autowired
	private UserRespository userRepo;
	
	@Value("${app.user.verificationTimeMinute:30}")
	private long verificationTimeMinute;

	@Override
	public Boolean execute(User user) throws AppException {
		log.debug("Customer details id {} ,version {}", user.getId());
		//String code = customer.getEmail().toLowerCase();
		user.setCodeSentCounter(0);
		user.setInvalidCodeAttemptCount(0);
		user.setVerificationEndTime(OffsetDateTime.now().plusMinutes(verificationTimeMinute));
		//redisTemplate.delete(code);
		userRepo.save(user);
		log.debug("ResetCustomerDetails execute method end");
		return Boolean.TRUE;
	
	}

}
