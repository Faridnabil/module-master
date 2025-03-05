package com.genz.master.modules.pricepromos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PricePromoRequestDto {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    private BigDecimal discount; // Opsional

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
}