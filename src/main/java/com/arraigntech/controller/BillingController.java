package com.arraigntech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.arraigntech.billing.service.StripeService;
import com.arraigntech.billing.vo.CheckoutRequest;

@Controller
@RequestMapping("/payment")
public class BillingController {

	@Autowired
	private StripeService stripeService;

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkout(@RequestBody CheckoutRequest checkoutRequest, Model model) {
		model.addAttribute("amount", checkoutRequest.getAmount());
		model.addAttribute("stripePublicKey", stripeService.getProperties().getPublicKey());
		model.addAttribute("currency", checkoutRequest.getCurrencyType());
		return "checkout.html";
	}

}
