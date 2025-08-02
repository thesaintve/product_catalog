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
class RatioStockCriterialTest {

    private RatioStockCriterial ratioStockCriterial;

    @BeforeEach
    void setUp() {
        ratioStockCriterial = new RatioStockCriterial();
    }

    @Test
    @DisplayName("Debería calcular correctamente el valor de ordenamiento para un producto con ratio de stock")
    void generateOrderingCriterialValue_shouldCalculateCorrectValue() {
        double weight = 0.5;
        double expectedRatio = 0.8;
        ProductItem mockProduct = mock(ProductItem.class);
        when(mockProduct.ratioStock()).thenReturn(expectedRatio);
        DoubleSummaryStatistics summaryStatistics = mock(DoubleSummaryStatistics.class);

        // Act
        Double result = ratioStockCriterial.generateOrderingCriterialValue(mockProduct, weight, summaryStatistics);

        // Assert
        assertEquals(expectedRatio * weight, result, 0.001);
        verify(mockProduct, times(1)).ratioStock();
    }

    @Test
    @DisplayName("Debería retornar cero cuando el peso es cero")
    void generateOrderingCriterialValue_shouldReturnZeroWhenWeightIsZero() {
        ProductItem mockProduct = mock(ProductItem.class);
        when(mockProduct.ratioStock()).thenReturn(0.75);
        DoubleSummaryStatistics summaryStatistics = mock(DoubleSummaryStatistics.class);

        // Act
        Double result = ratioStockCriterial.generateOrderingCriterialValue(mockProduct, 0.0, summaryStatistics);

        // Assert
        assertEquals(0.0, result, 0.001);
    }

}