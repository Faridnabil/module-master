package com.genz.master.modules.pricepromos;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.genz.master.modules.pricepromos.dto.PricePromoRequestDto;
import com.genz.master.modules.products.ProductEntity;
import com.genz.master.modules.products.ProductRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PricePromoService {

    private final PricePromoRepository pricePromoRepository;

    private final ProductRepository productRepository;

    public List<PricePromoEntity> getAllPricePromos() {
        return pricePromoRepository.listAll();
    }

    @Transactional
    public PricePromoEntity create(PricePromoRequestDto request) {
        // Cek apakah produk ada
        Optional<ProductEntity> productOptional = productRepository.findByIdOptional(request.getProductId());
        if (productOptional.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        PricePromoEntity pricePromo = new PricePromoEntity();
        pricePromo.setProduct(productOptional.get());
        pricePromo.setPrice(request.getPrice());
        pricePromo.setDiscount(request.getDiscount());
        pricePromo.setStartDate(request.getStartDate());
        pricePromo.setEndDate(request.getEndDate());

        pricePromoRepository.persist(pricePromo);
        return pricePromo;
    }

    public List<PricePromoEntity> getPricePromosByProductId(Long productId) {
        return pricePromoRepository.findByProductId(productId);
    }

    @Transactional
    public Optional<PricePromoEntity> update(Long id, PricePromoRequestDto pricePromoDto) {
        Optional<PricePromoEntity> existingPricePromoOptional = pricePromoRepository.findByIdOptional(id);
        if (existingPricePromoOptional.isPresent()) {
            PricePromoEntity existingPricePromo = existingPricePromoOptional.get();
            existingPricePromo.setPrice(pricePromoDto.getPrice());
            existingPricePromo.setDiscount(pricePromoDto.getDiscount());
            existingPricePromo.setStartDate(pricePromoDto.getStartDate());
            existingPricePromo.setEndDate(pricePromoDto.getEndDate());

            pricePromoRepository.persist(existingPricePromo);
            return Optional.of(existingPricePromo);
        }
        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        return pricePromoRepository.deleteById(id);
    }
}