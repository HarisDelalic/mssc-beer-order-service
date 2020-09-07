package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.beer_order.BeerOrderDto;

public interface BeerOrderManager {

    BeerOrderDto newBeerOrder(BeerOrderDto beerOrder);
    void failValidation(BeerOrderDto beerOrderDto);
    void passValidation(BeerOrderDto beerOrderDto);
    void passAllocation(BeerOrderDto beerOrderDto);
    void failAllocationWithException(BeerOrderDto beerOrderDto);
    void failAllocationNoInventory(BeerOrderDto beerOrderDto);
}
