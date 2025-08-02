package com.prueba.tecnica02.application.orderingcriterial;

import com.prueba.tecnica02.domain.model.ProductItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.DoubleSummaryStatistics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoldUnitsCriterialTest {

    private SoldUnitsCriterial soldUnitsCriterial;

    @BeforeEach
    void setUp() {
        soldUnitsCriterial = new SoldUnitsCriterial();
    }

    @Test
    @DisplayName("Debería calcular correctamente el valor de ordenamiento basado en unidades vendidas")
    void generateOrderingCriterialValue_shouldCalculateCorrectValue() {
        double weight = 0.5;
        long soldUnits = 100;
        double max = 150;
        double min = 20;

        ProductItem mockProduct = mock(ProductItem.class);
        when(mockProduct.soldUnits()).thenReturn(soldUnits);

        DoubleSummaryStatistics summaryStatistics = mock(DoubleSummaryStatistics.class);
        when(summaryStatistics.getMax()).thenReturn(max);
        when(summaryStatistics.getMin()).thenReturn(min);

        // Act
        Double result = soldUnitsCriterial.generateOrderingCriterialValue(mockProduct, weight, summaryStatistics);

        // Assert
        assertEquals(normalizedSoldUnits(soldUnits, max, min)  * weight, result, 0.001);
        verify(mockProduct, times(1)).soldUnits();
    }

    @Test
    @DisplayName("Debería retornar cero cuando el peso es cero")
    void generateOrderingCriterialValue_shouldReturnZeroWhenWeightIsZero() {
        double max = 150;
        double min = 20;

        ProductItem mockProduct = mock(ProductItem.class);
        when(mockProduct.soldUnits()).thenReturn(50L);

        DoubleSummaryStatistics summaryStatistics = mock(DoubleSummaryStatistics.class);
        when(summaryStatistics.getMax()).thenReturn(max);
        when(summaryStatistics.getMin()).thenReturn(min);

        // Act
        Double result = soldUnitsCriterial.generateOrderingCriterialValue(mockProduct, 0.0, summaryStatistics);

        // Assert
        assertEquals(0.0, result, 0.001);
    }

    @Test
    @DisplayName("Debería manejar correctamente el máximo valor de unidades vendidas")
    void generateOrderingCriterialValue_shouldHandleMaxIntegerValue() {
        double weight = 1.0;
        long soldUnits = Long.MAX_VALUE;
        double max = Long.MAX_VALUE;
        double min = 20;

        ProductItem mockProduct = mock(ProductItem.class);
        when(mockProduct.soldUnits()).thenReturn(Long.MAX_VALUE);

        DoubleSummaryStatistics summaryStatistics = mock(DoubleSummaryStatistics.class);
        when(summaryStatistics.getMax()).thenReturn(max);
        when(summaryStatistics.getMin()).thenReturn(min);

        // Act
        Double result = soldUnitsCriterial.generateOrderingCriterialValue(mockProduct, weight, summaryStatistics);

        // Assert
        assertEquals(normalizedSoldUnits(soldUnits, max, min) * weight, result, 0.001);
    }

    private Double normalizedSoldUnits(long soldUnits, double max, double min) {
        return (soldUnits - min) / (max - min);
    }
}