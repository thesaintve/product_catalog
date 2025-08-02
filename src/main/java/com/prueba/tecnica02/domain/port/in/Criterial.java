package com.prueba.tecnica02.domain.port.in;

import com.prueba.tecnica02.domain.model.ProductItem;

import java.util.DoubleSummaryStatistics;

public interface Criterial {
    Double generateOrderingCriterialValue(ProductItem productItem, double weight, DoubleSummaryStatistics summary);
}
