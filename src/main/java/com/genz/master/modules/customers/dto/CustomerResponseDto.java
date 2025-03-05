package com.genz.master.modules.customers.dto;

import com.genz.master.modules.customers.CustomerEntity;

import lombok.Data;

@Data
public class CustomerResponseDto {

    private Long id;
    private String name;
    private String email;
    private String noTelephone;
    private String address;

    public CustomerResponseDto(CustomerEntity user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.noTelephone = user.getNoTelephone();
        this.address = user.getAddress();
    }
}
