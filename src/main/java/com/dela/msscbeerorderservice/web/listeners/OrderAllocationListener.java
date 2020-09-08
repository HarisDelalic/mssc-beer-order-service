package com.dela.msscbeerorderservice.web.listeners;

import com.dela.brewery.events.OrderAllocationResponse;
import com.dela.brewery.models.beer_order.BeerOrderDto;
import com.dela.msscbeerorderservice.config.JmsConfig;
import com.dela.msscbeerorderservice.services.BeerOrderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderAllocationListener {
    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_RESPONSE_QUEUE)
    public void listenForAllocationResponse(OrderAllocationResponse orderAllocationResponse) {
        BeerOrderDto beerOrderDto = orderAllocationResponse.getBeerOrderDto();

        if(orderAllocationResponse.getError()) {
            beerOrderManager.failAllocationWithException(beerOrderDto);
        } else if(orderAllocationResponse.getPendingInventory()) {
            beerOrderManager.failAllocationNoInventory(beerOrderDto);
        } else {
            beerOrderManager.passAllocation(beerOrderDto);
        }
    }
}
