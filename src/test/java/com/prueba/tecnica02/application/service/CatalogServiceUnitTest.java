package com.prueba.tecnica02.application.service;


import com.prueba.tecnica02.domain.port.in.Criterial;
import com.prueba.tecnica02.domain.model.ProductItem;
import com.prueba.tecnica02.domain.port.in.SummaryStats;
import com.prueba.tecnica02.domain.port.out.ProductRepository;
import com.prueba.tecnica02.domain.model.CriterialWeightRequest;
import com.prueba.tecnica02.domain.model.ProductItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Map<String, Criterial> criterialByName;

    @Mock
    private Map<String, SummaryStats> summaryStatsByName;

    @Mock
    private Criterial ratioStockCriterial;

    @Mock
    private Criterial soldUnitsCriterial;

    @Mock
    private SummaryStats ratioStockSummaryStats;

    @Mock
    private SummaryStats soldUnitsSummaryStats;


    @InjectMocks
    private CatalogService catalogService;


    @BeforeEach
    void setUp() {
        catalogService = new CatalogService(productRepository, criterialByName, summaryStatsByName);
    }

    @Test
    void getOrderedProducts_shouldReturnOrderedProducts() {
        List<CriterialWeightRequest> criteria = List.of(
            new CriterialWeightRequest("ratioStock", 0.5),
            new CriterialWeightRequest("soldUnits", 0.5)
        );

        ProductItem product1 = new ProductItem(UUID.randomUUID(), "Product 1", 1, 4.5,10L, 0.3);
        ProductItem product2 = new ProductItem(UUID.randomUUID(), "Product 2", 2, 4.0,20L, 0.8);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(criterialByName.containsKey(anyString())).thenReturn(true);
        when(criterialByName.get("ratioStock")).thenReturn(ratioStockCriterial);
        when(criterialByName.get("soldUnits")).thenReturn(soldUnitsCriterial);
        when(summaryStatsByName.get("ratioStockSummaryStats")).thenReturn(ratioStockSummaryStats);
        when(summaryStatsByName.get("soldUnitsSummaryStats")).thenReturn(soldUnitsSummaryStats);
        when(ratioStockCriterial.generateOrderingCriterialValue(any(), anyDouble(), any()))
            .thenAnswer(inv -> {
                ProductItem p = inv.getArgument(0);
                Double weight = inv.getArgument(1);
                return p.ratioStock() * weight;
            });
        when(soldUnitsCriterial.generateOrderingCriterialValue(any(), anyDouble(), any()))
            .thenAnswer(inv -> {
                ProductItem p = inv.getArgument(0);
                Double weight = inv.getArgument(1);
                return p.soldUnits() * weight;
            });

        when(ratioStockSummaryStats.getSummary(any()))
            .thenAnswer(inv -> {
                return new DoubleSummaryStatistics();
            });
        when(soldUnitsSummaryStats.getSummary(any()))
            .thenAnswer(inv -> {
                return new DoubleSummaryStatistics();
            });


        // Act
        List<ProductItemResponse> result = catalogService.getOrderedProducts(criteria);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).orderCriterialValue() > result.get(1).orderCriterialValue());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getOrderedProducts_shouldThrowWhenInvalidCriteria() {
        List<CriterialWeightRequest> invalidCriteria = List.of(
                new CriterialWeightRequest("invalid", 1.0)
        );

        when(criterialByName.containsKey("invalid")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> catalogService.getOrderedProducts(invalidCriteria));
    }

    @Test
    void getProducts_shouldReturnAllProducts() {
        ProductItem product1 = new ProductItem(UUID.randomUUID(), "Product 1", 1, 4.5,10L, 0.8);
        ProductItem product2 = new ProductItem(UUID.randomUUID(), "Product 2", 2, 4.0,20L, 0.3);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // Act
        List<ProductItemResponse> result = catalogService.getProducts();

        // Assert
        assertEquals(2, result.size());
        assertNull(result.get(0).orderCriterialValue());
        verify(productRepository, times(1)).findAll();
    }
}