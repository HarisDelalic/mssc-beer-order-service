package com.dela.brewery.events;

import com.dela.brewery.models.beer_order.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderValidationRequest implements Serializable {

    private static long serialVersionUID = -8152665195502943379L;

    private BeerOrderDto beerOrderDto;
}
