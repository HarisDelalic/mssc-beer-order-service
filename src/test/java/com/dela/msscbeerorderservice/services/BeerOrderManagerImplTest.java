package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.beer_order.BeerOrderDto;
import com.dela.msscbeerorderservice.domain.BeerOrder;
import com.dela.msscbeerorderservice.domain.BeerOrderStatus;
import com.dela.msscbeerorderservice.repositories.BeerOrderRepository;
import com.dela.msscbeerorderservice.util.BeerOrderDtoCreator;
import com.dela.msscbeerorderservice.web.mappers.BeerOrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BeerOrderManagerImplTest {

    @Autowired
    BeerOrderManager beerOrderManager;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerOrderMapper beerOrderMapper;

    @BeforeEach
    void setUp() {
        beerOrderRepository.deleteAll();
    }

    @Test
    void when_dataIsValid_newBeerOrderIsCreated() {
        BeerOrderDto beerOrderDto = beerOrderManager.newBeerOrder(BeerOrderDtoCreator.withDefaultValues());

        assertEquals(1, beerOrderRepository.count());

        beerOrderRepository.findById(beerOrderDto.getId()).ifPresent(border ->
                assertEquals(BeerOrderStatus.VALIDATION_PENDING, border.getOrderStatus()));

    }

    @Test
    void when_failValidationIsCalled_statusIsValidationException() {
        BeerOrder beerOrder = BeerOrder.builder()
                .orderStatus(BeerOrderStatus.VALIDATION_PENDING)
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        beerOrderManager.failValidation(beerOrderMapper.beerOrderToDto(savedBeerOrder));

        beerOrderRepository.findById(savedBeerOrder.getId()).ifPresent(border ->
                assertEquals(BeerOrderStatus.VALIDATION_EXCEPTION, border.getOrderStatus()));

    }

    @Test
    void when_passValidationIsCalled_statusIsValidated() {
        BeerOrder beerOrder = BeerOrder.builder()
                .orderStatus(BeerOrderStatus.VALIDATION_PENDING)
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        beerOrderManager.passValidation(beerOrderMapper.beerOrderToDto(savedBeerOrder));

        beerOrderRepository.findById(savedBeerOrder.getId()).ifPresent(border ->
                assertEquals(BeerOrderStatus.ALLOCATION_PENDING, border.getOrderStatus()));

    }
}