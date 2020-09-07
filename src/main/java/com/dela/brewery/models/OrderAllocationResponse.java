package com.dela.brewery.models;

import com.dela.brewery.models.beer_order.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class OrderAllocationResponse {

    private BeerOrderDto beerOrderDto;
    private boolean error;
    private boolean pendingInventory;
}
