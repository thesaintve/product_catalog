package com.prueba.tecnica02.infrastructure.adapter.in.rest;

import com.prueba.tecnica02.domain.model.CriterialWeightRequest;
import com.prueba.tecnica02.domain.model.ProductItemResponse;
import com.prueba.tecnica02.domain.port.in.CatalogUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CatalogController.class)
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public CatalogUseCase catalogService() {
            return Mockito.mock(CatalogUseCase.class);
        }
    }
    @Autowired
    private CatalogUseCase catalogUseCase;

    @Test
    void getOrderedProducts_shouldReturn200WhenValidRequest() throws Exception {
        String requestBody = """
            [
                {"criterial": "ratioStock", "weight": 0.5},
                {"criterial": "soldUnits", "weight": 0.5}
            ]
            """;

        List<ProductItemResponse> mockResponse = List.of(
                new ProductItemResponse("1", "Product A", 0.85),
                new ProductItemResponse("2", "Product B", 0.75)
        );

        given(catalogUseCase.getOrderedProducts(anyList())).willReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/products/ordered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].productName").value("Product A"))
                .andExpect(jsonPath("$[0].orderCriterialValue").value(0.85));
    }

    @Test
    void getProducts_shouldReturn200WithProductsList() throws Exception {
        // Arrange
        List<ProductItemResponse> mockResponse = List.of(
                new ProductItemResponse("1", "Product A", null),
                new ProductItemResponse("2", "Product B", null)
        );

        given(catalogUseCase.getProducts()).willReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].orderedCriterialValue").doesNotExist());
    }
}