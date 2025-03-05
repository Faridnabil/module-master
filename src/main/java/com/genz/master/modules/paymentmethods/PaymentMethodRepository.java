package com.genz.master.modules.paymentmethods;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class PaymentMethodRepository implements PanacheRepository<PaymentMethodEntity> {

    public Optional<PaymentMethodEntity> findByName(String name) {
        return find("name", name).firstResultOptional();
    }
}