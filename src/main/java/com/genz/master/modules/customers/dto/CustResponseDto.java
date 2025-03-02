package com.genz.master.modules.customers.dto;

import com.genz.master.modules.customers.CustEntity;

import lombok.Data;

@Data
public class CustResponseDto {

    private Long id;
    private String name;
    private String email;
    private String noTelephone;
    private String address;

    public CustResponseDto(CustEntity user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.noTelephone = user.getNoTelephone();
        this.address = user.getAddress();
    }
}
