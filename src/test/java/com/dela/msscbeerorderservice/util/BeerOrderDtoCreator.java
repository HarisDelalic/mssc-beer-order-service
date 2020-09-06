package com.dela.msscbeerorderservice.util;

import com.dela.brewery.models.BeerOrderDto;

public class BeerOrderDtoCreator {

    public static BeerOrderDto withDefaultValues() {
        return BeerOrderDto.builder().build();
    }
}
