package com.genz.master.modules.pricepromos.dto;

import com.genz.master.modules.pricepromos.PricePromoEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PricePromoResponseDto {

    private Long id;
    private Long productId;
    private BigDecimal price;
    private BigDecimal discount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;

    public PricePromoResponseDto(PricePromoEntity pricePromo) {
        this.id = pricePromo.getId();
        this.productId = pricePromo.getProduct().getId();
        this.price = pricePromo.getPrice();
        this.discount = pricePromo.getDiscount();
        this.startDate = pricePromo.getStartDate();
        this.endDate = pricePromo.getEndDate();
        this.isActive = pricePromo.isActive();
    }
}