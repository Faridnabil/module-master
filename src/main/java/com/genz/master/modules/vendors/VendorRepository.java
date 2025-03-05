package com.genz.master.modules.vendors;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class VendorRepository implements PanacheRepository<VendorEntity> {

    public Optional<VendorEntity> findByName(String name) {
        return find("name", name).firstResultOptional();
    }
}