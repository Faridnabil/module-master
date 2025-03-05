package com.genz.master.modules.customers;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<CustomerEntity> {

    public Optional<CustomerEntity> findByName(String name) {
        return find("name", name).firstResultOptional();
    }
}
