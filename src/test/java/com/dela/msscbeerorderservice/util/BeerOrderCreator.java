package com.dela.msscbeerorderservice.util;

import com.dela.msscbeerorderservice.domain.BeerOrder;
import com.dela.msscbeerorderservice.domain.BeerOrderLine;

import java.util.HashSet;
import java.util.Set;

public class BeerOrderCreator {
    public static BeerOrder withDefault() {
        return BeerOrder.builder().build();
    }

    public static BeerOrder withBeerOrderLines() {
        BeerOrder beerOrder = BeerOrder.builder()
                .build();

        Set<BeerOrderLine> lines = new HashSet<>();
        lines.add(BeerOrderLine.builder()
//                .beerId(BeerDtoCreator.BEER_ID)
                .upc(BeerDtoCreator.BEER_UPC)
                .orderQuantity(1)
                .beerOrder(beerOrder)
                .build());

        beerOrder.setBeerOrderLines(lines);

        return beerOrder;
    }
}
