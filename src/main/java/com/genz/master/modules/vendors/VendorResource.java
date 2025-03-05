package com.genz.master.modules.vendors;

import com.genz.master.common.ApiResponse;
import com.genz.master.modules.vendors.dto.VendorRequestDto;
import com.genz.master.modules.vendors.dto.VendorResponseDto;
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

@Path("vendors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RequiredArgsConstructor
public class VendorResource {

    private final VendorService service;

    private final JsonWebToken jwt;

    @GET
    @RolesAllowed({ "Admin", "Vendor" })
    public ApiResponse<List<VendorResponseDto>> getAllVendors() {
        List<VendorResponseDto> vendors = service.getAllVendors().stream()
                .map(VendorResponseDto::new)
                .collect(Collectors.toList());
        return ApiResponse.ok(vendors, "Vendors retrieved successfully");
    }

    @POST
    @RolesAllowed("Admin")
    public ApiResponse<VendorResponseDto> create(@Valid VendorRequestDto vendorDto) {
        try {
            VendorEntity createdVendorEntity = service.create(vendorDto, true); // Buat vendor dengan akun user
            VendorResponseDto createdVendor = new VendorResponseDto(createdVendorEntity);
            return ApiResponse.ok(createdVendor, "Vendor created successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to create vendor: " + e.getMessage());
        }
    }

    @GET
    @Path("/{name}")
    @RolesAllowed({ "Admin", "Vendor" })
    public ApiResponse<VendorResponseDto> getVendorByName(@PathParam("name") String name) {
        // Jika user bukan Admin, hanya bisa mengakses data sendiri
        if (!jwt.getGroups().contains("Admin") && !jwt.getName().equals(name)) {
            return ApiResponse.error("You are not authorized to access this resource");
        }

        return service.getVendorByName(name)
                .map(vendor -> ApiResponse.ok(new VendorResponseDto(vendor), "Vendor found"))
                .orElse(ApiResponse.notFound("Vendor not found"));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("Admin")
    public ApiResponse<VendorResponseDto> update(@PathParam("id") Long id, @Valid VendorRequestDto vendorDto) {
        try {
            Optional<VendorEntity> updatedVendorOptional = service.update(id, vendorDto);
            if (updatedVendorOptional.isPresent()) {
                VendorResponseDto updatedVendor = new VendorResponseDto(updatedVendorOptional.get());
                return ApiResponse.ok(updatedVendor, "Vendor updated successfully");
            } else {
                return ApiResponse.notFound("Vendor not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to update vendor: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Admin")
    public ApiResponse<Void> delete(@PathParam("id") Long id) {
        try {
            boolean deleted = service.delete(id);
            if (deleted) {
                return ApiResponse.ok(null, "Vendor deleted successfully");
            } else {
                return ApiResponse.notFound("Vendor not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete vendor: " + e.getMessage());
        }
    }
}