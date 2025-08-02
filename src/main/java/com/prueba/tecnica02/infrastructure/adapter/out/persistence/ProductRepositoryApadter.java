package com.prueba.tecnica02.infrastructure.adapter.out.persistence;

import com.prueba.tecnica02.domain.model.ProductItem;
import com.prueba.tecnica02.domain.port.out.ProductRepository;
import com.prueba.tecnica02.infrastructure.adapter.out.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductRepositoryApadter implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public List<ProductItem> findAll() {
        log.info("Obteniendo lista de productos desde BD");

        return toModel(jpaProductRepository.findAll());
    }

    private List<ProductItem> toModel(List<ProductEntity> entities) {
        return entities.stream().map(this::toModel).toList();
    }

    private ProductItem toModel(ProductEntity entity) {
        return new ProductItem(
                entity.getId(),
                entity.getProductName(),
                entity.getRatings(),
                entity.getPrice(),
                entity.getSoldUnits(),
                entity.getRatioStock()
            );
    }
}
