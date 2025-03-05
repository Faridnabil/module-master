package com.genz.master.modules.paymentmethods;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payment_methods")
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Nama metode pembayaran (contoh: "Transfer Bank", "Kartu Kredit")

    @Column(nullable = false)
    private String description; // Deskripsi metode pembayaran

    @Column(nullable = false)
    private boolean isActive = true; // Status aktif/non-aktif
}