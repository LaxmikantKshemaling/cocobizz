package com.cocobizz.cocobizz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users user;

    private BigDecimal totalAmount;

    private Boolean active = true;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CartItem> items = new java.util.ArrayList<>(); // ✅ FIX

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        if(this.totalAmount == null){
            this.totalAmount = BigDecimal.ZERO;
        }
    }
}