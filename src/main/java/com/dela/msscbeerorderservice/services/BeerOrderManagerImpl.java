package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.BeerOrderDto;
import com.dela.msscbeerorderservice.domain.BeerOrder;
import com.dela.msscbeerorderservice.domain.BeerOrderEvent;
import com.dela.msscbeerorderservice.domain.BeerOrderStatus;
import com.dela.msscbeerorderservice.repositories.BeerOrderRepository;
import com.dela.msscbeerorderservice.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

//import org.springframework.messaging.support.MessageBuilder;

@RequiredArgsConstructor
@Service
public class BeerOrderManagerImpl implements BeerOrderManager {
    private static final String beerOrderID = "beer-order-id";

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final StateMachineFactory<BeerOrderStatus, BeerOrderEvent> stateMachineFactory;

    @Override
    public BeerOrderDto newBeerOrder(BeerOrderDto beerOrderDto) {
        BeerOrder beerOrder = beerOrderMapper.dtoToBeerOrder(beerOrderDto);
        beerOrder.setOrderStatus(BeerOrderStatus.NEW);

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        sendEvent(beerOrderDto, BeerOrderEvent.VALIDATE_ORDER);

        return beerOrderMapper.beerOrderToDto(savedBeerOrder);
    }

    private void sendEvent(BeerOrderDto beerOrderDto, BeerOrderEvent event) {
        StateMachine<BeerOrderStatus, BeerOrderEvent> sm = build(beerOrderDto);

        Message<BeerOrderEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(beerOrderID, beerOrderDto.getId())
                .build();

        sm.sendEvent(message);
    }

    private StateMachine<BeerOrderStatus, BeerOrderEvent> build(BeerOrderDto beerOrderDto) {

        StateMachine<BeerOrderStatus, BeerOrderEvent> sm = stateMachineFactory.getStateMachine(beerOrderDto.getId());

        sm.stop();

        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.resetStateMachine
                            (new DefaultStateMachineContext<BeerOrderStatus, BeerOrderEvent>
                                    (BeerOrderStatus.valueOf(beerOrderDto.getOrderStatus()),
                                            null,
                                            null,
                                            null));
                });

        sm.start();

        return sm;
    }


}
