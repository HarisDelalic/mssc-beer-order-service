package com.dela.beer.order.service.config;

import com.dela.beer.order.service.domain.BeerOrderEvent;
import com.dela.beer.order.service.domain.BeerOrderStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class BeerOrderStateMachineConfig extends StateMachineConfigurerAdapter<BeerOrderStatus, BeerOrderEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<BeerOrderStatus, BeerOrderEvent> states) throws Exception {
        states
                .withStates()
                .initial(BeerOrderStatus.NEW)
                .states(EnumSet.allOf(BeerOrderStatus.class))
                .end(BeerOrderStatus.DELIVERED)
                .end(BeerOrderStatus.VALIDATION_EXCEPTION)
                .end(BeerOrderStatus.ALLOCATION_EXCEPTION)
                .end(BeerOrderStatus.DELIVERY_EXCEPTION)
                .end(BeerOrderStatus.PICKED_UP);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<BeerOrderStatus, BeerOrderEvent> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(BeerOrderStatus.NEW).target(BeerOrderStatus.VALIDATED)
                    .event(BeerOrderEvent.VALIDATE_ORDER)
                    .and()
                .withExternal()
                    .source(BeerOrderStatus.NEW).target(BeerOrderStatus.VALIDATION_EXCEPTION)
                    .event(BeerOrderEvent.VALIDATE_ORDER)
                    .and()
                .withExternal()
                    .source(BeerOrderStatus.VALIDATED).target(BeerOrderStatus.ALLOCATED)
                    .event(BeerOrderEvent.VALIDATION_PASSED)
                    .and()
                .withExternal()
                    .source(BeerOrderStatus.VALIDATED).target(BeerOrderStatus.ALLOCATION_EXCEPTION)
                    .event(BeerOrderEvent.ALLOCATION_FAILED)
                    .and()
                .withExternal()
                    .source(BeerOrderStatus.VALIDATED).target(BeerOrderStatus.PENDING_INVENTORY)
                    .event(BeerOrderEvent.ALLOCATION_NO_INVENTORY)
                    .and()
                .withExternal()
                    .source(BeerOrderStatus.ALLOCATED).target(BeerOrderStatus.PICKED_UP)
                    .event(BeerOrderEvent.BEER_ORDER_PICKED_UP);
    }
}
