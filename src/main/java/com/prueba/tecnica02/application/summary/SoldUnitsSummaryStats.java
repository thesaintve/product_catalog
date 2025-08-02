package com.prueba.tecnica02.application.summary;

import com.prueba.tecnica02.domain.model.ProductItem;
import com.prueba.tecnica02.domain.port.in.SummaryStats;
import org.springframework.stereotype.Component;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Component("soldUnitsSummaryStats")
public class SoldUnitsSummaryStats implements SummaryStats {
    @Override
    public DoubleSummaryStatistics getSummary(List<ProductItem> productItems) {
        return productItems.stream().mapToDouble(ProductItem::soldUnits).summaryStatistics();
    }
}
