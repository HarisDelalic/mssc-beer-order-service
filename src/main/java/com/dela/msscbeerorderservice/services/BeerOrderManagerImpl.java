package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.beer_order.BeerOrderDto;
import com.dela.msscbeerorderservice.domain.BeerOrder;
import com.dela.msscbeerorderservice.domain.BeerOrderEvent;
import com.dela.msscbeerorderservice.domain.BeerOrderStatus;
import com.dela.msscbeerorderservice.repositories.BeerOrderRepository;
import com.dela.msscbeerorderservice.sm.interceptors.BeerOrderInterceptor;
import com.dela.msscbeerorderservice.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
@Slf4j
public class BeerOrderManagerImpl implements BeerOrderManager {
    public static final String BEER_ORDER_ID = "beer-order-id";

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final StateMachineFactory<BeerOrderStatus, BeerOrderEvent> stateMachineFactory;
    private final BeerOrderInterceptor beerOrderInterceptor;

    @Transactional
    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {
        beerOrder.setOrderStatus(BeerOrderStatus.NEW);

        BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);

        sendEvent(savedBeerOrder, BeerOrderEvent.VALIDATE_ORDER);

        return savedBeerOrder;
    }

    @Transactional
    @Override
    public void failValidation(UUID beerOrderId) {
        beerOrderRepository.findById(beerOrderId).ifPresent(beerOrder -> {
            sendEvent(beerOrder, BeerOrderEvent.VALIDATION_FAILED);
        });
    }

    @Transactional
    @Override
    public void passValidation(UUID beerOrderId) {
        beerOrderRepository.findById(beerOrderId).ifPresent(beerOrder -> {
            sendEvent(beerOrder, BeerOrderEvent.VALIDATION_PASSED);

            BeerOrder validatedBeerOrder = beerOrderRepository.findById(beerOrder.getId()).get();

            sendEvent(validatedBeerOrder, BeerOrderEvent.ALLOCATE_ORDER);

        });
    }

    @Override
    public void passAllocation(BeerOrderDto beerOrderDto) {
        beerOrderRepository.findById(beerOrderDto.getId()).ifPresentOrElse(beerOrder -> {
            sendEvent(beerOrder, BeerOrderEvent.ALLOCATION_SUCCESS);
            awaitForStatus(beerOrder.getId(), BeerOrderStatus.ALLOCATED);
            updateAllocatedQty(beerOrderDto);
        }, () -> log.error("Order Id Not Found: " + beerOrderDto.getId()));
    }

    @Override
    public void failAllocationWithException(BeerOrderDto beerOrderDto) {
        sendEvent(beerOrderMapper.dtoToBeerOrder(beerOrderDto), BeerOrderEvent.ALLOCATION_FAILED);
    }

    @Override
    public void failAllocationNoInventory(BeerOrderDto beerOrderDto) {
        sendEvent(beerOrderMapper.dtoToBeerOrder(beerOrderDto), BeerOrderEvent.ALLOCATION_NO_INVENTORY);

    }

    private void updateAllocatedQty(BeerOrderDto beerOrderDto) {
        Optional<BeerOrder> allocatedOrderOptional = beerOrderRepository.findById(beerOrderDto.getId());

        allocatedOrderOptional.ifPresentOrElse(allocatedOrder -> {
            allocatedOrder.getBeerOrderLines().forEach(beerOrderLine -> {
                beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {
                    if(beerOrderLine.getId() .equals(beerOrderLineDto.getId())){
                        beerOrderLine.setQuantityAllocated(beerOrderLineDto.getOrderQuantity());
                    }
                });
            });

            beerOrderRepository.saveAndFlush(allocatedOrder);
        }, () -> log.error("Order Not Found. Id: " + beerOrderDto.getId()));
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

    private void awaitForStatus(UUID beerOrderId, BeerOrderStatus statusEnum) {

        AtomicBoolean found = new AtomicBoolean(false);
        AtomicInteger loopCount = new AtomicInteger(0);

        while (!found.get()) {
            if (loopCount.incrementAndGet() > 10) {
                found.set(true);
                log.debug("Loop Retries exceeded");
            }

            beerOrderRepository.findById(beerOrderId).ifPresentOrElse(beerOrder -> {
                if (beerOrder.getOrderStatus().equals(statusEnum)) {
                    found.set(true);
                    log.debug("Order Found");
                } else {
                    log.debug("Order Status Not Equal. Expected: " + statusEnum.name() + " Found: " + beerOrder.getOrderStatus().name());
                }
            }, () -> {
                log.debug("Order Id Not Found");
            });

            if (!found.get()) {
                try {
                    log.debug("Sleeping for retry");
                    Thread.sleep(100);
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
    }


}
