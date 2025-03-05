package com.genz.master.modules.paymentmethods.dto;

import com.genz.master.modules.paymentmethods.PaymentMethodEntity;
import lombok.Data;

@Data
public class PaymentMethodResponseDto {

    private Long id;
    private String name;
    private String description;
    private boolean isActive;

    public PaymentMethodResponseDto(PaymentMethodEntity paymentMethod) {
        this.id = paymentMethod.getId();
        this.name = paymentMethod.getName();
        this.description = paymentMethod.getDescription();
        this.isActive = paymentMethod.isActive();
    }
}