package com.genz.master.modules.customers;

import com.genz.master.common.ApiResponse;
import com.genz.master.modules.customers.dto.CustRequestDto;
import com.genz.master.modules.customers.dto.CustResponseDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RequiredArgsConstructor
public class CustResource {

    private final CustService service;

    private final JsonWebToken jwt;

    @GET
    @RolesAllowed({ "Admin", "User" })
    public ApiResponse<List<CustResponseDto>> getAllCustomers() {
        List<CustResponseDto> custs = service.getAllCustomers().stream()
                .map(CustResponseDto::new)
                .collect(Collectors.toList());
        return ApiResponse.ok(custs, "Customers retrieved successfully");
    }

    @POST
    @RolesAllowed("Admin")
    public ApiResponse<CustResponseDto> create(@Valid CustRequestDto custDto) {
        try {
            // Tentukan apakah customer memiliki akun user
            boolean hasUserAccount = custDto.getUser() != null;

            CustEntity createdCustEntity = service.create(custDto, hasUserAccount);
            CustResponseDto createdCust = new CustResponseDto(createdCustEntity);
            return ApiResponse.ok(createdCust, "Customer created successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to create customer: " + e.getMessage());
        }
    }

    @GET
    @Path("/{name}")
    @RolesAllowed({ "Admin", "User" })
    public ApiResponse<CustResponseDto> getCustByName(@PathParam("name") String name) {
        // Jika user bukan Admin, hanya bisa mengakses data sendiri
        if (!jwt.getGroups().contains("Admin") && !jwt.getName().equals(name)) {
            return ApiResponse.error("You are not authorized to access this resource");
        }

        return service.getCustByName(name)
                .map(cust -> ApiResponse.ok(new CustResponseDto(cust), "Customer found"))
                .orElse(ApiResponse.notFound("Customer not found"));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("Admin")
    public ApiResponse<CustResponseDto> update(@PathParam("id") Long id, @Valid CustRequestDto custDto) {
        try {
            Optional<CustEntity> updatedCustOptional = service.update(id, custDto);
            if (updatedCustOptional.isPresent()) {
                CustResponseDto updatedCust = new CustResponseDto(updatedCustOptional.get());
                return ApiResponse.ok(updatedCust, "Customer updated successfully");
            } else {
                return ApiResponse.notFound("Customer not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to update customer: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Admin")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        try {
            boolean deleted = service.delete(id);
            if (deleted) {
                return ApiResponse.ok(null, "Customer deleted successfully");
            } else {
                return ApiResponse.notFound("Customer not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete customer: " + e.getMessage());
        }
    }
}