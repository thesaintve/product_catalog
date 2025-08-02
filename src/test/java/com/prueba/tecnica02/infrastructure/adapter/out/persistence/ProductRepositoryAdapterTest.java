package com.prueba.tecnica02.infrastructure.adapter.out.persistence;

import com.prueba.tecnica02.domain.model.ProductItem;
import com.prueba.tecnica02.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @Mock
    private JpaProductRepository jpaProductRepository;

    @InjectMocks
    private ProductRepositoryApadter productRepositoryAdapter;

    private ProductEntity testEntity;
    private ProductItem expectedModel;

    @BeforeEach
    void setUp() {
        UUID id = UUID.randomUUID();

        testEntity = new ProductEntity();
        testEntity.setId(id);
        testEntity.setProductName("Test Product");
        testEntity.setRatings(4);
        testEntity.setPrice(100.0);
        testEntity.setSoldUnits(50L);
        testEntity.setRatioStock(0.8);

        expectedModel = new ProductItem(
                id,
                "Test Product",
                4,
                100.0,
                50L,
                0.8
        );
    }

    @Test
    @DisplayName("findAll debería retornar lista de ProductItem cuando hay productos")
    void findAll_shouldReturnListOfProductItemsWhenProductsExist() {
        when(jpaProductRepository.findAll()).thenReturn(List.of(testEntity));

        // Act
        List<ProductItem> result = productRepositoryAdapter.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedModel, result.get(0));
        verify(jpaProductRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll debería retornar lista vacía cuando no hay productos")
    void findAll_shouldReturnEmptyListWhenNoProducts() {
        when(jpaProductRepository.findAll()).thenReturn(List.of());

        // Act
        List<ProductItem> result = productRepositoryAdapter.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaProductRepository, times(1)).findAll();
    }
}