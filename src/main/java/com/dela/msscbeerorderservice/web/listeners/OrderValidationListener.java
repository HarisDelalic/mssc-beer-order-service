package com.dela.msscbeerorderservice.web.listeners;

import com.dela.brewery.events.OrderValidationResponse;
import com.dela.brewery.models.beer_order.OrderValidationResultDto;
import com.dela.msscbeerorderservice.config.JmsConfig;
import com.dela.msscbeerorderservice.services.BeerOrderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidationListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESULT_QUEUE)
    public void getOrderValidationResponse(OrderValidationResponse orderValidationResponse) {

        OrderValidationResultDto orderValidationResultDto = orderValidationResponse.getOrderValidationResultDto();

            if(orderValidationResultDto.isValid()) {
                beerOrderManager.passValidation(orderValidationResultDto.getId());
            } else {
                beerOrderManager.failValidation(orderValidationResultDto.getId());
            }


    }
}
