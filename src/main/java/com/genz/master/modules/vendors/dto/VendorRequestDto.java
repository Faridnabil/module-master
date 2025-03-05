package com.genz.master.modules.vendors.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendorRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Nomor Telephone is required")
    private String noTelephone;

    @NotBlank(message = "Address is required")
    private String address;

    private UserCreateRequestDto user;
}