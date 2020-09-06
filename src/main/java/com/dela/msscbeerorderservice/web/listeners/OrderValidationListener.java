package com.dela.msscbeerorderservice.web.listeners;

import com.dela.brewery.events.OrderValidationResponse;
import com.dela.brewery.models.beer_order.OrderValidationResultDto;
import com.dela.msscbeerorderservice.config.JmsConfig;
import com.dela.msscbeerorderservice.repositories.BeerOrderRepository;
import com.dela.msscbeerorderservice.services.BeerOrderManager;
import com.dela.msscbeerorderservice.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidationListener {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderManager beerOrderManager;
    private final BeerOrderMapper beerOrderMapper;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESULT_QUEUE)
    public void getOrderValidationResponse(OrderValidationResponse orderValidationResponse) {

        OrderValidationResultDto orderValidationResultDto = orderValidationResponse.getOrderValidationResultDto();

        beerOrderRepository.findById(orderValidationResultDto.getId()).ifPresent(beerOrder -> {
            boolean isValid = orderValidationResultDto.isValid();

            if(isValid) {
                beerOrderManager.passValidation(beerOrderMapper.beerOrderToDto(beerOrder));
            } else {
                beerOrderManager.failValidation(beerOrderMapper.beerOrderToDto(beerOrder));
            }
        });


    }
}
