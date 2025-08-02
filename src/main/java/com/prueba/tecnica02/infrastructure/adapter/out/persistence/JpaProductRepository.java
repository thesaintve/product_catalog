package com.prueba.tecnica02.infrastructure.adapter.out.persistence;

import com.prueba.tecnica02.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {
}
