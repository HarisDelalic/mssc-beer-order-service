package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.BeerDto;

import java.util.Optional;

public interface BeerService {
    Optional<BeerDto> getBeerByUpc(String upc);
    Optional<BeerDto> getBeerById(String upc);
}
