package com.genz.master.modules.customers;

import com.genz.master.common.ApiResponse;
import com.genz.master.modules.customers.dto.CustomerRequestDto;
import com.genz.master.modules.customers.dto.CustomerResponseDto;
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
public class CustomerResource {

    private final CustomerService service;

    private final JsonWebToken jwt;

    @GET
    @RolesAllowed({ "Admin", "User" })
    public ApiResponse<List<CustomerResponseDto>> getAllCustomers() {
        List<CustomerResponseDto> custs = service.getAllCustomers().stream()
                .map(CustomerResponseDto::new)
                .collect(Collectors.toList());
        return ApiResponse.ok(custs, "Customers retrieved successfully");
    }

    @POST
    @RolesAllowed("Admin")
    public ApiResponse<CustomerResponseDto> create(@Valid CustomerRequestDto custDto) {
        try {
            // Tentukan apakah customer memiliki akun user
            boolean hasUserAccount = custDto.getUser() != null;

            CustomerEntity createdCustEntity = service.create(custDto, hasUserAccount);
            CustomerResponseDto createdCust = new CustomerResponseDto(createdCustEntity);
            return ApiResponse.ok(createdCust, "Customer created successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to create customer: " + e.getMessage());
        }
    }

    @GET
    @Path("/{name}")
    @RolesAllowed({ "Admin", "User" })
    public ApiResponse<CustomerResponseDto> getCustByName(@PathParam("name") String name) {
        // Jika user bukan Admin, hanya bisa mengakses data sendiri
        if (!jwt.getGroups().contains("Admin") && !jwt.getName().equals(name)) {
            return ApiResponse.error("You are not authorized to access this resource");
        }

        return service.getCustByName(name)
                .map(cust -> ApiResponse.ok(new CustomerResponseDto(cust), "Customer found"))
                .orElse(ApiResponse.notFound("Customer not found"));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("Admin")
    public ApiResponse<CustomerResponseDto> update(@PathParam("id") Long id, @Valid CustomerRequestDto custDto) {
        try {
            Optional<CustomerEntity> updatedCustOptional = service.update(id, custDto);
            if (updatedCustOptional.isPresent()) {
                CustomerResponseDto updatedCust = new CustomerResponseDto(updatedCustOptional.get());
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