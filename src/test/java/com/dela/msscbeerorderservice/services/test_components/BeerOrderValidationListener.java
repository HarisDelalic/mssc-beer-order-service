package com.dela.msscbeerorderservice.services.test_components;

import com.dela.brewery.events.OrderValidationRequest;
import com.dela.brewery.events.OrderValidationResponse;
import com.dela.brewery.models.beer_order.OrderValidationResultDto;
import com.dela.msscbeerorderservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BeerOrderValidationListener {
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_REQUEST_QUEUE)
    public void list(OrderValidationRequest request) {
        boolean isValid = true;

        OrderValidationResponse response = new OrderValidationResponse(OrderValidationResultDto.builder()
                .id(request.getBeerOrderDto().getId())
                .isValid(isValid)
                .build());

        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESULT_QUEUE, response);

    }
}
