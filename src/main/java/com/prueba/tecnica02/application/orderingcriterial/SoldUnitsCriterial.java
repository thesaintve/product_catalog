package com.prueba.tecnica02.application.orderingcriterial;

import com.prueba.tecnica02.domain.model.ProductItem;
import com.prueba.tecnica02.domain.port.in.Criterial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.DoubleSummaryStatistics;

@Slf4j
@Component("soldUnits")
public class SoldUnitsCriterial implements Criterial {
    @Override
    public Double generateOrderingCriterialValue(ProductItem productItem, double weight, DoubleSummaryStatistics summary) {
        return normalizedSoldUnitsValue(productItem, summary) * weight;
    }

    private Double normalizedSoldUnitsValue(ProductItem p, DoubleSummaryStatistics s) {
        // valor normalizado = (valor - min) / (max - min)
        return (p.soldUnits() - s.getMin()) / (s.getMax() - s.getMin());
    }

}
