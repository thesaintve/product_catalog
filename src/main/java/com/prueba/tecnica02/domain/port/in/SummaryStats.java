package com.prueba.tecnica02.domain.port.in;

import com.prueba.tecnica02.domain.model.ProductItem;

import java.util.DoubleSummaryStatistics;
import java.util.List;

public interface SummaryStats {

    DoubleSummaryStatistics getSummary(List<ProductItem> productItems);

}
