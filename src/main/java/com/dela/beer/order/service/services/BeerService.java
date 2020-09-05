package com.dela.beer.order.service.services;

import com.dela.beer.order.service.web.model.BeerDto;

import java.util.Optional;

public interface BeerService {
    Optional<BeerDto> getBeerByUpc(String upc);
    Optional<BeerDto> getBeerById(String upc);
}
