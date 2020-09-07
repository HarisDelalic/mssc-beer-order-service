package com.dela.brewery.events;

import com.dela.brewery.models.beer_order.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
@Builder
public class AllocateOrderRequest implements Serializable {

    private static long serialVersionUID = 8411646669375942152L;

    private BeerOrderDto beerOrderDto;

}
