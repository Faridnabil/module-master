package com.genz.master.modules.pricepromos;

import com.genz.master.modules.products.ProductEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "price_promos")
public class PricePromoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product; // Relasi ke ProductEntity

    @Column(nullable = false)
    private BigDecimal price; // Harga normal

    @Column
    private BigDecimal discount; // Diskon (opsional)

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate; // Tanggal mulai promo

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate; // Tanggal berakhir promo

    @Column(nullable = false)
    private boolean isActive = true; // Status aktif/non-aktif
}