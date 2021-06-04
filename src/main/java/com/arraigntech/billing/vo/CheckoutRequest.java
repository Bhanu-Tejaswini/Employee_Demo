package com.arraigntech.billing.vo;

public class CheckoutRequest {

	private Double amount;
	private ChargeRequest.Currency currencyType;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public ChargeRequest.Currency getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(ChargeRequest.Currency currencyType) {
		this.currencyType = currencyType;
	}

}
