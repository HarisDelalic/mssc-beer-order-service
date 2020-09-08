package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.beer_order.BeerOrderDto;
import com.dela.msscbeerorderservice.domain.BeerOrder;

import java.util.UUID;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
    void failValidation(UUID beerOrderDto);
    void passValidation(UUID beerOrderDto);
    void passAllocation(BeerOrderDto beerOrderDto);
    void failAllocationWithException(BeerOrderDto beerOrderDto);
    void failAllocationNoInventory(BeerOrderDto beerOrderDto);
}
