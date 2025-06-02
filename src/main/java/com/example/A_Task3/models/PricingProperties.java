package com.example.A_Task3.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PricingProperties {
    private BigDecimal extra_days_rental_price;
    private BigDecimal insurance_fees;
}
