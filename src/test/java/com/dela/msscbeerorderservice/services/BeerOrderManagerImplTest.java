package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.BeerOrderDto;
import com.dela.msscbeerorderservice.domain.BeerOrderStatus;
import com.dela.msscbeerorderservice.repositories.BeerOrderRepository;
import com.dela.msscbeerorderservice.util.BeerOrderDtoCreator;
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
}