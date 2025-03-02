package com.genz.master.modules.users.dto;

import com.genz.master.modules.users.UserEntity;

import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private String role;
    private boolean statusAktif;

    public UserResponseDto(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.statusAktif = user.isStatusAktif();
    }
}
