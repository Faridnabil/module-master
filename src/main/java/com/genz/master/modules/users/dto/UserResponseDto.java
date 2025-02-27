package com.genz.master.modules.users.dto;

import com.genz.master.modules.users.UserEntity;

import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;

    public UserResponseDto(UserEntity user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
