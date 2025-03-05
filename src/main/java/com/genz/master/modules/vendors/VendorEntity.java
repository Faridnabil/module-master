package com.genz.master.modules.vendors;

import java.util.List;

import com.genz.master.modules.products.ProductEntity;
import com.genz.master.modules.users.UserEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "vendors")
public class VendorEntity {

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

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<ProductEntity> products;

    @Column(nullable = false)
    private boolean isDeleted = false;
}