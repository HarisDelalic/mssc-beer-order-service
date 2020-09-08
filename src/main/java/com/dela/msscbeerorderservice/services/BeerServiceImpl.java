package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.BeerDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@ConfigurationProperties(value = "com.dela", ignoreUnknownFields = false)
public class BeerServiceImpl implements BeerService {
    public static final String BEER_BY_UPC_API = "/api/v1/beers/beerUpc/";
    public static final String BEER_BY_UUID_API = "/api/v1/beers/";

    private final RestTemplate restTemplate;
    private String beerServiceHostApi;

    public BeerServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void setBeerServiceHostApi(String beerServiceHostApi) {
        this.beerServiceHostApi = beerServiceHostApi;
    }

    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {
        return Optional.ofNullable(restTemplate.getForObject(beerServiceHostApi + BEER_BY_UPC_API + upc, BeerDto.class));
    }

    @Override
    public Optional<BeerDto> getBeerById(String id) {
        return  Optional.ofNullable(restTemplate.getForObject(beerServiceHostApi + BEER_BY_UUID_API + id, BeerDto.class));
    }
}
