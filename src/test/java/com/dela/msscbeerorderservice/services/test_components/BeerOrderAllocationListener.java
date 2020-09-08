package com.dela.msscbeerorderservice.services.test_components;


import com.dela.brewery.events.OrderAllocationRequest;
import com.dela.brewery.events.OrderAllocationResponse;
import com.dela.msscbeerorderservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BeerOrderAllocationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_REQUEST_QUEUE)
    public void listenAndRespond(OrderAllocationRequest orderAllocationRequest) {

        OrderAllocationResponse response = new OrderAllocationResponse(
                orderAllocationRequest.getBeerOrderDto(),
                false,
                false
        );

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESPONSE_QUEUE, response);
    }
}
