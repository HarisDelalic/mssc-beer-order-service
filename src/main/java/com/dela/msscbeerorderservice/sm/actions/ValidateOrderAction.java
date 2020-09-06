package com.dela.msscbeerorderservice.sm.actions;

import com.dela.brewery.events.ValidateOrderRequest;
import com.dela.brewery.models.BeerOrderDto;
import com.dela.msscbeerorderservice.config.JmsConfig;
import com.dela.msscbeerorderservice.domain.BeerOrderEvent;
import com.dela.msscbeerorderservice.repositories.BeerOrderRepository;
import com.dela.msscbeerorderservice.services.BeerOrderManagerImpl;
import com.dela.msscbeerorderservice.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidateOrderAction implements Action {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public void execute(StateContext stateContext) {
        Message<BeerOrderEvent> message = stateContext.getMessage();

        Optional<UUID> beerOrderId = Optional.of((UUID) message.getHeaders().get(BeerOrderManagerImpl.BEER_ORDER_ID));

        beerOrderId.flatMap(beerOrderRepository::findById)
                .ifPresent(beerOrder -> {
                    BeerOrderDto beerOrderDto = beerOrderMapper.beerOrderToDto(beerOrder);

                    jmsTemplate.convertAndSend(
                            JmsConfig.VALIDATE_ORDER_REQUEST_QUEUE,
                            new ValidateOrderRequest(beerOrderDto)
                    );
                });

    }
}
