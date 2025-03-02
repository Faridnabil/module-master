package com.genz.master.modules.customers;

import com.genz.master.modules.users.UserEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "customers")
public class CustEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "no_telephone", nullable = false)
    private String noTelephone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private boolean isDeleted = false;
}