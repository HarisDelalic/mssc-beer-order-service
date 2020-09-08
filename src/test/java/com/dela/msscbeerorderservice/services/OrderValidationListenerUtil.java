package com.dela.msscbeerorderservice.services;

//import com.dela.brewery.events.OrderValidationRequest;
//import com.dela.brewery.events.OrderValidationResponse;
//import com.dela.brewery.models.beer_order.OrderValidationResultDto;
//import com.dela.msscbeerorderservice.config.JmsConfig;
//import lombok.RequiredArgsConstructor;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
public class OrderValidationListenerUtil {

//    private final JmsTemplate jmsTemplate;
//
//    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_REQUEST_QUEUE)
//    public void listen(OrderValidationRequest request) {
//
//        System.out.println("############### I RUN");
//
//        OrderValidationResultDto result = OrderValidationResultDto.builder()
//                .id(request.getBeerOrderDto().getId())
//                .isValid(true)
//                .build();
//
//        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESULT_QUEUE,
//                new OrderValidationResponse(result)
//        );
//
//
//    }
}
