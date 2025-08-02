package com.prueba.tecnica02.domain.model;

import java.util.UUID;

public record ProductItem(
        UUID id,
        String productName,
        Integer ratings,
        Double price,
        Long soldUnits,
        Double ratioStock
) {}

