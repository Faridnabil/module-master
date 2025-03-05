package com.genz.master.modules.products;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.genz.master.modules.products.dto.ProductRequestDto;
import com.genz.master.modules.vendors.VendorEntity;
import com.genz.master.modules.vendors.VendorRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final VendorRepository vendorRepository;

    private final JsonWebToken jwt;

    public List<ProductEntity> getAllProducts() {
        if (!jwt.getGroups().contains("Admin")) {
            // Pastikan vendorId ada di token JWT
            Long vendorId = jwt.getClaim("vendorId");
            if (vendorId == null) {
                throw new IllegalArgumentException("Vendor ID not found in token");
            }
            return productRepository.findByVendorId(vendorId);
        }
        return productRepository.listAll();
    }

    @Transactional
    public ProductEntity create(ProductRequestDto request) {
        // Cek apakah vendor ada
        Optional<VendorEntity> vendorOptional = vendorRepository.findByIdOptional(request.getVendorId());
        if (vendorOptional.isEmpty()) {
            throw new IllegalArgumentException("Vendor not found");
        }

        ProductEntity product = new ProductEntity();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setVendor(vendorOptional.get());

        // Simpan produk ke database
        productRepository.persist(product);
        return product;
    }

    public Optional<ProductEntity> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    public List<ProductEntity> getProductsByVendorId(Long vendorId) {
        return productRepository.findByVendorId(vendorId);
    }

    @Transactional
    public Optional<ProductEntity> update(Long id, ProductRequestDto productDto) {
        Optional<ProductEntity> existingProductOptional = productRepository.findByIdOptional(id);
        if (existingProductOptional.isPresent()) {
            ProductEntity existingProduct = existingProductOptional.get();

            // Perbarui data produk
            existingProduct.setName(productDto.getName());
            existingProduct.setDescription(productDto.getDescription());
            existingProduct.setPrice(productDto.getPrice());

            // Perbarui vendor jika diubah
            if (productDto.getVendorId() != null) {
                Optional<VendorEntity> vendorOptional = vendorRepository.findByIdOptional(productDto.getVendorId());
                if (vendorOptional.isPresent()) {
                    existingProduct.setVendor(vendorOptional.get());
                } else {
                    throw new IllegalArgumentException("Vendor not found");
                }
            }

            // Simpan perubahan produk ke database
            productRepository.persist(existingProduct);
            return Optional.of(existingProduct);
        }
        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<ProductEntity> productOptional = productRepository.findByIdOptional(id);
        if (productOptional.isPresent()) {
            ProductEntity product = productOptional.get();

            // Soft delete produk
            product.setDeleted(true);
            productRepository.persist(product);

            return true;
        }
        return false;
    }
}