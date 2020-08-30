package guru.sfg.beer.order.service.web.mappers;

import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.services.BeerService;
import guru.sfg.beer.order.service.web.model.BeerOrderLineDto;
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
