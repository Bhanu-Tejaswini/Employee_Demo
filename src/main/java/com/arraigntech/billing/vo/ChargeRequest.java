package com.arraigntech.billing.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author manojkumarmathiarasan
 *
 */
@Data
@AllArgsConstructor
public class ChargeRequest {

    public enum Currency {
        EUR, USD, INR;
    }
    private String description;
    private int amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
}