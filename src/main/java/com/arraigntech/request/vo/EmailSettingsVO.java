package com.arraigntech.request.vo;

public class EmailSettingsVO {

	private Boolean systemAlerts;
	private Boolean monthlyStreamingReports;
	private Boolean instantStreamingReport;
	private Boolean promotions;
	private Boolean productUpdates;
	private Boolean blogDigest;

	public Boolean getSystemAlerts() {
		return systemAlerts;
	}

	public void setSystemAlerts(Boolean systemAlerts) {
		this.systemAlerts = systemAlerts;
	}

	public Boolean getMonthlyStreamingReports() {
		return monthlyStreamingReports;
	}

	public void setMonthlyStreamingReports(Boolean monthlyStreamingReports) {
		this.monthlyStreamingReports = monthlyStreamingReports;
	}

	public Boolean getInstantStreamingReport() {
		return instantStreamingReport;
	}

	public void setInstantStreamingReport(Boolean instantStreamingReport) {
		this.instantStreamingReport = instantStreamingReport;
	}

	public Boolean getPromotions() {
		return promotions;
	}

	public void setPromotions(Boolean promotions) {
		this.promotions = promotions;
	}

	public Boolean getProductUpdates() {
		return productUpdates;
	}

	public void setProductUpdates(Boolean productUpdates) {
		this.productUpdates = productUpdates;
	}

	public Boolean getBlogDigest() {
		return blogDigest;
	}

	public void setBlogDigest(Boolean blogDigest) {
		this.blogDigest = blogDigest;
	}

	public EmailSettingsVO(Boolean systemAlerts, Boolean monthlyStreamingReports, Boolean instantStreamingReport,
			Boolean promotions, Boolean productUpdates, Boolean blogDigest) {
		super();
		this.systemAlerts = systemAlerts;
		this.monthlyStreamingReports = monthlyStreamingReports;
		this.instantStreamingReport = instantStreamingReport;
		this.promotions = promotions;
		this.productUpdates = productUpdates;
		this.blogDigest = blogDigest;
	}

	public EmailSettingsVO() {
		
	}

	@Override
	public String toString() {
		return "EmailSettingsModel [systemAlerts=" + systemAlerts + ", monthlyStreamingReports="
				+ monthlyStreamingReports + ", instantStreamingReport=" + instantStreamingReport + ", promotions="
				+ promotions + ", productUpdates=" + productUpdates + ", blogDigest=" + blogDigest + "]";
	}
}