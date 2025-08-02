package com.prueba.tecnica02.domain.port.in;

import com.prueba.tecnica02.domain.model.CriterialWeightRequest;
import com.prueba.tecnica02.domain.model.ProductItemResponse;

import java.util.List;

public interface CatalogUseCase {

    List<ProductItemResponse> getOrderedProducts(List<CriterialWeightRequest> criterialWeightRequests);

    List<ProductItemResponse> getProducts();
}

