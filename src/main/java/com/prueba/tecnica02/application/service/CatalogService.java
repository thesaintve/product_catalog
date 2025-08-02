package com.prueba.tecnica02.application.service;

import com.prueba.tecnica02.domain.model.CriterialWeightRequest;
import com.prueba.tecnica02.domain.model.ProductItem;
import com.prueba.tecnica02.domain.model.ProductItemResponse;
import com.prueba.tecnica02.domain.port.in.Criterial;
import com.prueba.tecnica02.domain.port.in.CatalogUseCase;
import com.prueba.tecnica02.domain.port.in.SummaryStats;
import com.prueba.tecnica02.domain.port.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogService implements CatalogUseCase {
    private static final String SUMMARY_STACTS_SUFFIX = "SummaryStats";
    private static final String SUMMARY_RESULT_SUFFIX = "SummaryResult";

    @Value("${weights.error.margin}")
    private double errMargin;

    private final ProductRepository productRepository;
    private final Map<String, Criterial> strategyCriterialByName;
    private final Map<String, SummaryStats> strategySummaryStatsByName;

    private final Map<String, DoubleSummaryStatistics> summaryResultByName = new HashMap<>();


    @Override
    public List<ProductItemResponse> getOrderedProducts(List<CriterialWeightRequest> criterialWeightRequests) {
        log.info("Validando {} criterios de ordenamiento", criterialWeightRequests.size());

        criterialValidate(criterialWeightRequests);
        weightsValidate(criterialWeightRequests);

        List<ProductItem> products = productRepository.findAll();

        loadSummaryStatisticData(criterialWeightRequests, products);

        log.info("Generando lista ordenada");
        return products.stream()
            .map(p -> {
                Double orderedCriterialValue = criterialWeightRequests.stream()
                    .mapToDouble(cw -> strategyCriterialByName.get(cw.criterial()).generateOrderingCriterialValue(p, cw.weight(), summaryResultByName.get(cw.criterial() + SUMMARY_RESULT_SUFFIX))).sum();

                return new ProductItemResponse(p.id().toString(), p.productName(), orderedCriterialValue );
            })
            .sorted(Comparator.comparing(ProductItemResponse::orderCriterialValue).reversed())
            .toList();
    }

    @Override
    public List<ProductItemResponse> getProducts() {
        log.info("Generando lista sin orden definido");
        return productRepository.findAll().stream().map(p -> new ProductItemResponse(p.id().toString(), p.productName(), null )).toList();
    }

    private void criterialValidate(List<CriterialWeightRequest> criterialWeightRequests) {
        if (criterialWeightRequests == null || criterialWeightRequests.isEmpty()) {
            throw new IllegalArgumentException("Debe haber al menos un criterios de ordenamiento");
        }

        List<String> invalidCriterialsName = criterialWeightRequests.stream()
            .map(CriterialWeightRequest::criterial)
            .filter(criterialName -> !strategyCriterialByName.containsKey(criterialName))
            .collect(Collectors.toList());

        if (!invalidCriterialsName.isEmpty()) {
            throw new IllegalArgumentException("Criterios de ordenamiento no válidos: " + String.join(", ", invalidCriterialsName));
        }
    }

    private void weightsValidate(List<CriterialWeightRequest> criterialWeightRequests) {
        double criterialWeightsSum = criterialWeightRequests.stream().mapToDouble(CriterialWeightRequest::weight).sum();
        if (criterialWeightsSum < minAllowed() || criterialWeightsSum > maxAllowed()) {
            throw new IllegalArgumentException("La suma de los pesos de todos los criterios de ordenamiento deben sumar 1");
        }
    }

    private void loadSummaryStatisticData(List<CriterialWeightRequest> criterialWeightRequests, List<ProductItem> products) {
        log.info("Generando resumen estadistico para normalizacion de datos");
        criterialWeightRequests.forEach(cwr -> {
            summaryResultByName.put(cwr.criterial() + SUMMARY_RESULT_SUFFIX, strategySummaryStatsByName.get(cwr.criterial() + SUMMARY_STACTS_SUFFIX).getSummary(products));
        });
        System.out.println(summaryResultByName);
    }

    private double maxAllowed(){
        return 1 + errMargin;
    }

    private double minAllowed(){
        return 1 - errMargin;
    }
}
