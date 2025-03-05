package com.genz.master.modules.paymentmethods;

import com.genz.master.common.ApiResponse;
import com.genz.master.modules.paymentmethods.dto.PaymentMethodRequestDto;
import com.genz.master.modules.paymentmethods.dto.PaymentMethodResponseDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("payment-methods")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RequiredArgsConstructor
public class PaymentMethodResource {

    private final PaymentMethodService service;

    @GET
    @RolesAllowed({ "Admin", "Vendor", "Customer" })
    public ApiResponse<List<PaymentMethodResponseDto>> getAllPaymentMethods() {
        List<PaymentMethodResponseDto> paymentMethods = service.getAllPaymentMethods().stream()
                .map(PaymentMethodResponseDto::new)
                .collect(Collectors.toList());
        return ApiResponse.ok(paymentMethods, "Payment methods retrieved successfully");
    }

    @POST
    @RolesAllowed("Admin")
    public ApiResponse<PaymentMethodResponseDto> create(@Valid PaymentMethodRequestDto paymentMethodDto) {
        try {
            PaymentMethodEntity createdPaymentMethodEntity = service.create(paymentMethodDto);
            PaymentMethodResponseDto createdPaymentMethod = new PaymentMethodResponseDto(createdPaymentMethodEntity);
            return ApiResponse.ok(createdPaymentMethod, "Payment method created successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to create payment method: " + e.getMessage());
        }
    }

    @GET
    @Path("/{name}")
    @RolesAllowed({ "Admin", "Vendor", "Customer" })
    public ApiResponse<PaymentMethodResponseDto> getPaymentMethodByName(@PathParam("name") String name) {
        return service.getPaymentMethodByName(name)
                .map(paymentMethod -> ApiResponse.ok(new PaymentMethodResponseDto(paymentMethod),
                        "Payment method found"))
                .orElse(ApiResponse.notFound("Payment method not found"));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("Admin")
    public ApiResponse<PaymentMethodResponseDto> update(@PathParam("id") Long id,
            @Valid PaymentMethodRequestDto paymentMethodDto) {
        try {
            Optional<PaymentMethodEntity> updatedPaymentMethodOptional = service.update(id, paymentMethodDto);
            if (updatedPaymentMethodOptional.isPresent()) {
                PaymentMethodResponseDto updatedPaymentMethod = new PaymentMethodResponseDto(
                        updatedPaymentMethodOptional.get());
                return ApiResponse.ok(updatedPaymentMethod, "Payment method updated successfully");
            } else {
                return ApiResponse.notFound("Payment method not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to update payment method: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Admin")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        try {
            boolean deleted = service.delete(id);
            if (deleted) {
                return ApiResponse.ok(null, "Payment method deleted successfully");
            } else {
                return ApiResponse.notFound("Payment method not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete payment method: " + e.getMessage());
        }
    }
}