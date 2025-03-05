package com.genz.master.modules.pricepromos;

import com.genz.master.common.ApiResponse;
import com.genz.master.modules.pricepromos.dto.PricePromoRequestDto;
import com.genz.master.modules.pricepromos.dto.PricePromoResponseDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("price-promos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RequiredArgsConstructor
public class PricePromoResource {

    private final PricePromoService service;

    @GET
    @RolesAllowed({ "Admin", "Vendor", "Customer" })
    public ApiResponse<List<PricePromoResponseDto>> getAllPricePromos() {
        List<PricePromoResponseDto> pricePromos = service.getAllPricePromos().stream()
                .map(PricePromoResponseDto::new)
                .collect(Collectors.toList());
        return ApiResponse.ok(pricePromos, "Price promos retrieved successfully");
    }

    @POST
    @RolesAllowed("Admin")
    public ApiResponse<PricePromoResponseDto> create(@Valid PricePromoRequestDto pricePromoDto) {
        try {
            PricePromoEntity createdPricePromoEntity = service.create(pricePromoDto);
            PricePromoResponseDto createdPricePromo = new PricePromoResponseDto(createdPricePromoEntity);
            return ApiResponse.ok(createdPricePromo, "Price promo created successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to create price promo: " + e.getMessage());
        }
    }

    @GET
    @Path("/product/{productId}")
    @RolesAllowed({ "Admin", "Vendor", "Customer" })
    public ApiResponse<List<PricePromoResponseDto>> getPricePromosByProductId(@PathParam("productId") Long productId) {
        List<PricePromoResponseDto> pricePromos = service.getPricePromosByProductId(productId).stream()
                .map(PricePromoResponseDto::new)
                .collect(Collectors.toList());
        return ApiResponse.ok(pricePromos, "Price promos retrieved successfully");
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("Admin")
    public ApiResponse<PricePromoResponseDto> update(@PathParam("id") Long id,
            @Valid PricePromoRequestDto pricePromoDto) {
        try {
            Optional<PricePromoEntity> updatedPricePromoOptional = service.update(id, pricePromoDto);
            if (updatedPricePromoOptional.isPresent()) {
                PricePromoResponseDto updatedPricePromo = new PricePromoResponseDto(updatedPricePromoOptional.get());
                return ApiResponse.ok(updatedPricePromo, "Price promo updated successfully");
            } else {
                return ApiResponse.notFound("Price promo not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to update price promo: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Admin")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        try {
            boolean deleted = service.delete(id);
            if (deleted) {
                return ApiResponse.ok(null, "Price promo deleted successfully");
            } else {
                return ApiResponse.notFound("Price promo not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete price promo: " + e.getMessage());
        }
    }
}