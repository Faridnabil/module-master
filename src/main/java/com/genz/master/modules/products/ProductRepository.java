package com.genz.master.modules.products;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<ProductEntity> {

    public Optional<ProductEntity> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public List<ProductEntity> findByVendorId(Long vendorId) {
        return list("vendor.id", vendorId); // Perbaiki query
    }
}