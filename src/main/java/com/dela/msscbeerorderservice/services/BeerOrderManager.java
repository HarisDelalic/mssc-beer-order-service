package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.BeerOrderDto;

public interface BeerOrderManager {

    BeerOrderDto newBeerOrder(BeerOrderDto beerOrder);
}
