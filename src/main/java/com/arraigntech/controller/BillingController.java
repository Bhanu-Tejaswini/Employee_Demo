package com.arraigntech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.arraigntech.billing.service.StripeService;
import com.arraigntech.billing.vo.ChargeRequest;
import com.arraigntech.billing.vo.ChargeRequest.Currency;
import com.arraigntech.billing.vo.CheckoutRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@Controller
@RequestMapping("/payment")
public class BillingController {

	@Autowired
	private StripeService stripeService;
	
	@PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Model model)
      throws StripeException {
        chargeRequest.setDescription(chargeRequest.toString());
        chargeRequest.setCurrency(Currency.INR);
        Charge charge = stripeService.charge(chargeRequest);
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "result";
    }

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkout(@RequestBody CheckoutRequest checkoutRequest, Model model) {
		model.addAttribute("amount", checkoutRequest.getAmount());
		model.addAttribute("stripePublicKey", stripeService.getProperties().getPublicKey());
		model.addAttribute("currency", checkoutRequest.getCurrencyType());
		return "checkout.html";
	}

}
