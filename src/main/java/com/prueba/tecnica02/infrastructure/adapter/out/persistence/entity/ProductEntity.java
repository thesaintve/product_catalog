package com.prueba.tecnica02.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@Entity(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String productName;
    private Integer ratings;
    private Double price;
    private Long soldUnits;
    private Double ratioStock;
}
