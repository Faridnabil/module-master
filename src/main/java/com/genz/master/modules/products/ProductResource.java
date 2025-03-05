package com.genz.master.modules.products;

import com.genz.master.common.ApiResponse;
import com.genz.master.modules.products.dto.ProductRequestDto;
import com.genz.master.modules.products.dto.ProductResponseDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RequiredArgsConstructor
public class ProductResource {

    private final ProductService service;

    @GET
    @RolesAllowed({ "Admin", "Vendor", "Customer" })
    public ApiResponse<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = service.getAllProducts().stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
        return ApiResponse.ok(products, "Products retrieved successfully");
    }

    @POST
    @RolesAllowed("Vendor")
    public ApiResponse<ProductResponseDto> create(@Valid ProductRequestDto productDto) {
        try {
            ProductEntity createdProductEntity = service.create(productDto);
            ProductResponseDto createdProduct = new ProductResponseDto(createdProductEntity);
            return ApiResponse.ok(createdProduct, "Product created successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to create product: " + e.getMessage());
        }
    }

    @GET
    @Path("/{name}")
    @RolesAllowed({ "Admin", "Vendor", "Customer" })
    public ApiResponse<ProductResponseDto> getProductByName(@PathParam("name") String name) {
        return service.getProductByName(name)
                .map(product -> ApiResponse.ok(new ProductResponseDto(product), "Product found"))
                .orElse(ApiResponse.notFound("Product not found"));
    }

    @GET
    @Path("/vendor/{vendorId}")
    @RolesAllowed({ "Admin", "Vendor" })
    public ApiResponse<List<ProductResponseDto>> getProductsByVendorId(@PathParam("vendorId") Long vendorId) {
        List<ProductResponseDto> products = service.getProductsByVendorId(vendorId).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
        return ApiResponse.ok(products, "Products retrieved successfully");
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("Vendor")
    public ApiResponse<ProductResponseDto> update(@PathParam("id") Long id, @Valid ProductRequestDto productDto) {
        try {
            Optional<ProductEntity> updatedProductOptional = service.update(id, productDto);
            if (updatedProductOptional.isPresent()) {
                ProductResponseDto updatedProduct = new ProductResponseDto(updatedProductOptional.get());
                return ApiResponse.ok(updatedProduct, "Product updated successfully");
            } else {
                return ApiResponse.notFound("Product not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to update product: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Vendor")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        try {
            boolean deleted = service.delete(id);
            if (deleted) {
                return ApiResponse.ok(null, "Product deleted successfully");
            } else {
                return ApiResponse.notFound("Product not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete product: " + e.getMessage());
        }
    }
}