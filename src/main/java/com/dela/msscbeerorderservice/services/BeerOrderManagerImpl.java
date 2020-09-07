package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.beer_order.BeerOrderDto;
import com.dela.msscbeerorderservice.domain.BeerOrder;
import com.dela.msscbeerorderservice.domain.BeerOrderEvent;
import com.dela.msscbeerorderservice.domain.BeerOrderStatus;
import com.dela.msscbeerorderservice.sm.interceptors.BeerOrderInterceptor;
import com.dela.msscbeerorderservice.repositories.BeerOrderRepository;
import com.dela.msscbeerorderservice.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BeerOrderManagerImpl implements BeerOrderManager {
    public static final String BEER_ORDER_ID = "beer-order-id";

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final StateMachineFactory<BeerOrderStatus, BeerOrderEvent> stateMachineFactory;
    private final BeerOrderInterceptor beerOrderInterceptor;

    @Override
    public BeerOrderDto newBeerOrder(BeerOrderDto beerOrderDto) {
        BeerOrder beerOrder = beerOrderMapper.dtoToBeerOrder(beerOrderDto);
        beerOrder.setOrderStatus(BeerOrderStatus.NEW);

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        sendEvent(savedBeerOrder, BeerOrderEvent.VALIDATE_ORDER);

        return beerOrderMapper.beerOrderToDto(savedBeerOrder);
    }

    @Transactional
    @Override
    public void failValidation(BeerOrderDto beerOrderDto) {
        sendEvent(beerOrderMapper.dtoToBeerOrder(beerOrderDto), BeerOrderEvent.VALIDATION_FAILED);
    }

    @Transactional
    @Override
    public void passValidation(BeerOrderDto beerOrderDto) {
        sendEvent(beerOrderMapper.dtoToBeerOrder(beerOrderDto), BeerOrderEvent.VALIDATION_PASSED);

        BeerOrder validatedBeerOrder = beerOrderRepository.findOneById(beerOrderDto.getId());

        sendEvent(validatedBeerOrder, BeerOrderEvent.ALLOCATE_ORDER);
    }

    private void sendEvent(BeerOrder beerOrder, BeerOrderEvent event) {
        StateMachine<BeerOrderStatus, BeerOrderEvent> sm = build(beerOrder);

        Message<BeerOrderEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(BEER_ORDER_ID, beerOrder.getId())
                .build();

        sm.sendEvent(message);
    }

    private StateMachine<BeerOrderStatus, BeerOrderEvent> build(BeerOrder beerOrder) {

        StateMachine<BeerOrderStatus, BeerOrderEvent> sm = stateMachineFactory.getStateMachine(beerOrder.getId());

        sm.stop();

        sm.getStateMachineAccessor()

                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(beerOrderInterceptor);
                    sma.resetStateMachine
                            (new DefaultStateMachineContext<BeerOrderStatus, BeerOrderEvent>
                                    (beerOrder.getOrderStatus(),
                                            null,
                                            null,
                                            null));
                });



        sm.start();

        return sm;
    }


}
