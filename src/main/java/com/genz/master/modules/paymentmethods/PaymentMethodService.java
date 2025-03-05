package com.genz.master.modules.paymentmethods;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.genz.master.modules.paymentmethods.dto.PaymentMethodRequestDto;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethodEntity> getAllPaymentMethods() {
        return paymentMethodRepository.listAll();
    }

    @Transactional
    public PaymentMethodEntity create(PaymentMethodRequestDto request) {
        PaymentMethodEntity paymentMethod = new PaymentMethodEntity();
        paymentMethod.setName(request.getName());
        paymentMethod.setDescription(request.getDescription());

        paymentMethodRepository.persist(paymentMethod);
        return paymentMethod;
    }

    public Optional<PaymentMethodEntity> getPaymentMethodByName(String name) {
        return paymentMethodRepository.findByName(name);
    }

    @Transactional
    public Optional<PaymentMethodEntity> update(Long id, PaymentMethodRequestDto paymentMethodDto) {
        Optional<PaymentMethodEntity> existingPaymentMethodOptional = paymentMethodRepository.findByIdOptional(id);
        if (existingPaymentMethodOptional.isPresent()) {
            PaymentMethodEntity existingPaymentMethod = existingPaymentMethodOptional.get();
            existingPaymentMethod.setName(paymentMethodDto.getName());
            existingPaymentMethod.setDescription(paymentMethodDto.getDescription());

            paymentMethodRepository.persist(existingPaymentMethod);
            return Optional.of(existingPaymentMethod);
        }
        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        return paymentMethodRepository.deleteById(id);
    }
}