package com.dela.msscbeerorderservice.web.mappers;

import com.dela.msscbeerorderservice.domain.BeerOrderLine;
import com.dela.msscbeerorderservice.services.BeerService;
import com.dela.brewery.models.BeerOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper {

    @Autowired
    private BeerOrderLineMapper beerOrderLineMapper;

    @Autowired
    private BeerService beerFetcherService;

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
        BeerOrderLineDto beerOrderLineDto = beerOrderLineMapper.beerOrderLineToDto(line);

        beerFetcherService.getBeerByUpc(line.getUpc()).ifPresent(
                beerDto -> {
                    beerOrderLineDto.setBeerId(beerDto.getId());
                    beerOrderLineDto.setBeerName(beerDto.getBeerName());
                    beerOrderLineDto.setPrice(beerDto.getPrice());
                    beerOrderLineDto.setBeerStyle(beerDto.getBeerStyle());
                }
        );
        return beerOrderLineDto;
    }
}
