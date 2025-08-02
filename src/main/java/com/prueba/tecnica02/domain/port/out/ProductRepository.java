package com.prueba.tecnica02.domain.port.out;

import com.prueba.tecnica02.domain.model.ProductItem;

import java.util.List;

public interface ProductRepository {
    List<ProductItem> findAll();
}
