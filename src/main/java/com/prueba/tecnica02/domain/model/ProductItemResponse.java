package com.prueba.tecnica02.domain.model;


public record ProductItemResponse(
        String id,
        String productName,
        Double orderCriterialValue
) { }
