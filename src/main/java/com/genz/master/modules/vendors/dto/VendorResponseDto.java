package com.genz.master.modules.vendors.dto;

import com.genz.master.modules.vendors.VendorEntity;
import lombok.Data;

@Data
public class VendorResponseDto {

    private Long id;
    private String name;
    private String email;
    private String noTelephone;
    private String address;

    public VendorResponseDto(VendorEntity vendor) {
        this.id = vendor.getId();
        this.name = vendor.getName();
        this.email = vendor.getEmail();
        this.noTelephone = vendor.getNoTelephone();
        this.address = vendor.getAddress();
    }
}