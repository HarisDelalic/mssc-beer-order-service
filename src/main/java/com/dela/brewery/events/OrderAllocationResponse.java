package com.dela.brewery.events;

import com.dela.brewery.models.beer_order.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderAllocationResponse implements Serializable {

    private static long serialVersionUID = -8411646669373942152L;

    private BeerOrderDto beerOrderDto;
    private Boolean error;
    private Boolean pendingInventory;
}
