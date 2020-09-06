package com.dela.brewery.events;

import com.dela.brewery.models.BeerOrderDto;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
public class BeerOrderDtoEvent implements Serializable {

    static final long serialVersionUID = 3179343486497208908L;

    private final BeerOrderDto beerOrderDto;
}
