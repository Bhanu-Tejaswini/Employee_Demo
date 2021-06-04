package com.arraigntech.billing.vo;

import lombok.Data;

@Data
public class CheckoutRequest {

	private Double amount;
	private ChargeRequest.Currency currencyType;
}
