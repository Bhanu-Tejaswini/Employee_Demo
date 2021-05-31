package com.arraigntech.utility;

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
public class UpdateInvalidCodeCount implements Action<User, Integer> {
	public static final Logger log = LoggerFactory.getLogger(UpdateInvalidCodeCount.class);
	
	@Value("${app.user.restrictionCount:5}") 
    private int customerRestrictionCount;
	
	@Autowired
	private UserRespository userRepo;

	@Override
	public Integer execute(User user) throws AppException {
		log.debug("Customer details id {} ,version {}", user.getId());
		Integer count = user.getInvalidCodeAttemptCount() == null ? 0 : user.getInvalidCodeAttemptCount();
		count++;
		// Reset count to 1 when lock_end_time expired
		if (count > customerRestrictionCount && user.isAccountNonExpired()) {
			count = 1;
		}
		// Saving into db
		user.setInvalidCodeAttemptCount(count);
		userRepo.save(user);
		return count;
	}
	
}
