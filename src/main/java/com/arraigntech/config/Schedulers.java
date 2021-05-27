package com.arraigntech.config;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.arraigntech.entity.StreamTarget;
import com.arraigntech.entity.Streams;
import com.arraigntech.repository.StreamRepository;
import com.arraigntech.repository.StreamTargetRepository;

/**
 * @author Bhaskara S
 *
 */
@Configuration
@EnableScheduling
public class Schedulers {
	

	@Autowired
	private StreamRepository streamRepo;
	
	@Autowired
	private StreamTargetRepository streamTargetRepo;
	
	@Scheduled(cron = "0 0 0 1/1 * ? ")
	public void deleteStreams() {
		Date now = new Date();
		Date deleteDate = getDeleteDate(now, 30);
		System.out.println(deleteDate);
		List<Streams> streamList = streamRepo.findByCreatedAtBefore(deleteDate);
		streamRepo.deleteInBatch(streamList);
	}
	
	@Scheduled(cron = "0 0 0 1/1 * ? ")
	public void deleteStreamTargets() {
		Date now = new Date();
		Date deleteDate = getDeleteDate(now, 30);
		System.out.println(deleteDate);
		List<StreamTarget> streamTargetList = streamTargetRepo.findByCreatedAtBefore(deleteDate);
		streamTargetRepo.deleteInBatch(streamTargetList);
	}

	public static Date getDeleteDate(Date now, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(calendar.DATE, -days);
		Date delete = calendar.getTime();
		return delete;
	}

}
