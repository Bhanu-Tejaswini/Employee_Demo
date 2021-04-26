package com.arraigntech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraigntech.entity.Channels;
import com.arraigntech.entity.UpdateTitle;

public interface UpdateTitleRepository extends JpaRepository<UpdateTitle, String> {

	UpdateTitle findByChannel(Channels channel);
}
