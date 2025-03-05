package com.genz.master.modules.products.dto;

import com.genz.master.modules.products.ProductEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String namaVendor;

    public ProductResponseDto(ProductEntity product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.namaVendor = product.getVendor().getName();
    }
}