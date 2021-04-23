package com.arraigntech.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "email_settings")
public class EmailSettings extends VSBaseModel {

	@Column(name = "system_alerts")
	@Type(type = "numeric_boolean")
	private boolean systemAlerts;

	@Column(name = "monthly_streaming_reports")
	@Type(type = "numeric_boolean")
	private boolean monthlyStreamingReports;

	@Column(name = "instant_streaming_report")
	@Type(type = "numeric_boolean")
	private boolean instantStreamingReport;

	@Column(name = "promotions")
	@Type(type = "numeric_boolean")
	private boolean promotions;

	@Column(name = "product_updates")
	@Type(type = "numeric_boolean")
	private boolean productUpdates;

	@Column(name = "blog_digest")
	@Type(type = "numeric_boolean")
	private boolean blogDigest;

	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	private User user;

	public EmailSettings() {

	}

	public boolean isSystemAlerts() {
		return systemAlerts;
	}

	public void setSystemAlerts(boolean systemAlerts) {
		this.systemAlerts = systemAlerts;
	}

	public boolean isMonthlyStreamingReports() {
		return monthlyStreamingReports;
	}

	public void setMonthlyStreamingReports(boolean monthlyStreamingReports) {
		this.monthlyStreamingReports = monthlyStreamingReports;
	}

	public boolean isInstantStreamingReport() {
		return instantStreamingReport;
	}

	public void setInstantStreamingReport(boolean instantStreamingReport) {
		this.instantStreamingReport = instantStreamingReport;
	}

	public boolean isPromotions() {
		return promotions;
	}

	public void setPromotions(boolean promotions) {
		this.promotions = promotions;
	}

	public boolean isProductUpdates() {
		return productUpdates;
	}

	public void setProductUpdates(boolean productUpdates) {
		this.productUpdates = productUpdates;
	}

	public boolean isBlogDigest() {
		return blogDigest;
	}

	public void setBlogDigest(boolean blogDigest) {
		this.blogDigest = blogDigest;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
