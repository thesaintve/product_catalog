package com.prueba.tecnica02.infrastructure.adapter.in.rest;

import com.prueba.tecnica02.domain.model.CriterialWeightRequest;
import com.prueba.tecnica02.domain.model.ProductItemResponse;
import com.prueba.tecnica02.domain.port.in.CatalogUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Productos", description = "Consultas de catalogo de productos")
public class CatalogController {

    private final CatalogUseCase catalogUseCase;

    @Operation(
        summary = "Obtener productos ordenados por criterios de peso",
        description = "Retorna una lista de productos ordenados según los criterios de peso especificados en la solicitud."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Productos ordenados retornados exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductItemResponse.class, type = "array")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida (ej. criterios vacíos o pesos incorrectos)"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno al procesar la solicitud"
        )
    })
    @PostMapping("/ordered")
    public ResponseEntity<List<ProductItemResponse>> getOrderedProducts(@Valid @RequestBody List<CriterialWeightRequest> criterialsRequest) {
        log.info("Obteniendo lista de productos ordenados por {} criterios", criterialsRequest.size());

        List<ProductItemResponse> productItemsResponse = catalogUseCase.getOrderedProducts(criterialsRequest);
        return ResponseEntity.status(HttpStatus.OK).body(productItemsResponse);
    }

    @Operation(
        summary = "Obtener todos los productos",
        description = "Retorna una lista completa de productos sin ningún orden o filtro aplicado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductItemResponse.class, type = "array")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor al recuperar los productos"
        )
    })
    @GetMapping
    public ResponseEntity<List<ProductItemResponse>> getProducts() {
        log.info("Obteniendo lista de productos sin orden definido");

        List<ProductItemResponse> productItemsResponse = catalogUseCase.getProducts();
        return ResponseEntity.status(HttpStatus.OK).body(productItemsResponse);
    }
}
