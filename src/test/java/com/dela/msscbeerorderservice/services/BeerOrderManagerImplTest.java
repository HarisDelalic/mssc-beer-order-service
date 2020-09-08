package com.dela.msscbeerorderservice.services;

import com.dela.brewery.models.BeerDto;
import com.dela.msscbeerorderservice.domain.BeerOrder;
import com.dela.msscbeerorderservice.domain.BeerOrderLine;
import com.dela.msscbeerorderservice.domain.BeerOrderStatus;
import com.dela.msscbeerorderservice.repositories.BeerOrderRepository;
import com.dela.msscbeerorderservice.util.BeerDtoCreator;
import com.dela.msscbeerorderservice.util.BeerOrderCreator;
import com.dela.msscbeerorderservice.web.mappers.BeerOrderMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(WireMockExtension.class)
class BeerOrderManagerImplTest {

    @Autowired
    BeerOrderManager beerOrderManager;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerOrderMapper beerOrderMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8083);

    @BeforeEach
    void setUp() {
        beerOrderRepository.deleteAll();

        wireMockRule.start();
    }

    @AfterEach
    void tearDown() {
        wireMockRule.stop();
    }

    @Test
    void when_dataIsValid_newBeerOrderIsCreated() {
        BeerOrder beerOrder = beerOrderManager.newBeerOrder(BeerOrderCreator.withDefault());

        assertEquals(1, beerOrderRepository.count());

        beerOrderRepository.findById(beerOrder.getId()).ifPresent(border ->
                assertEquals(BeerOrderStatus.VALIDATION_PENDING, border.getOrderStatus()));

    }

    @Test
    void when_failValidationIsCalled_statusIsValidationException() {
        BeerOrder beerOrder = BeerOrder.builder()
                .orderStatus(BeerOrderStatus.VALIDATION_PENDING)
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        beerOrderManager.failValidation(savedBeerOrder.getId());

        beerOrderRepository.findById(savedBeerOrder.getId()).ifPresent(border ->
                assertEquals(BeerOrderStatus.ALLOCATED, border.getOrderStatus()));

    }

    @Test
    void when_passValidationIsCalled_statusIsValidated() {
        BeerOrder beerOrder = BeerOrder.builder()
                .orderStatus(BeerOrderStatus.VALIDATION_PENDING)
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        beerOrderManager.passValidation(savedBeerOrder.getId());

        beerOrderRepository.findById(savedBeerOrder.getId()).ifPresent(border ->
                assertEquals(BeerOrderStatus.ALLOCATION_PENDING, border.getOrderStatus()));

    }

    @Test
    void when_dataIsValidAndWithOrderLines_newBeerOrderIsCreated() throws JsonProcessingException {
        wireMockRule.stubFor(get(BeerServiceImpl.BEER_BY_UPC_API + BeerDtoCreator.BEER_UPC).willReturn(okJson(objectMapper.writeValueAsString(
                BeerDto.builder().build()
        ))));

        BeerOrder beerOrder = beerOrderManager.newBeerOrder(BeerOrderCreator.withBeerOrderLines());

        await().untilAsserted(() -> {
            beerOrderRepository.findById(beerOrder.getId()).ifPresent((foundOrder) -> {
                assertEquals(BeerOrderStatus.ALLOCATED, foundOrder.getOrderStatus());
            });
        });

        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            BeerOrderLine line = foundOrder.getBeerOrderLines().iterator().next();
            assertEquals(line.getOrderQuantity(), line.getQuantityAllocated());
        });
    }
}