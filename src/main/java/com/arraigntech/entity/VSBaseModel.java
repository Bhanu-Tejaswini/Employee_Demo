package com.arraigntech.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@MappedSuperclass
public abstract class VSBaseModel {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(unique = true)
	protected String id;

	protected Date createdAt;

	protected Date updatedAt;

	protected String createdBy;

	protected String updatedBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@PreUpdate
	public void preUpdate() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		this.setUpdatedAt(new Date());
		this.setUpdatedBy(authentication.getName());
	}

	@PrePersist
	public void prePersist() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		this.setCreatedAt(new Date());
		this.setCreatedBy(authentication.getName());
	}
}
