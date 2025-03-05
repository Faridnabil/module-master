package com.genz.master.modules.pricepromos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PricePromoRepository implements PanacheRepository<PricePromoEntity> {

    public List<PricePromoEntity> findByProductId(Long productId) {
        return list("product.id", productId);
    }
}