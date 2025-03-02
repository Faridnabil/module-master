package com.genz.master.modules.customers;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustRepository implements PanacheRepository<CustEntity> {

    public Optional<CustEntity> findByName(String name) {
        return find("name", name).firstResultOptional();
    }
}
