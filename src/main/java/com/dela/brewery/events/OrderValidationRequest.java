package com.dela.brewery.events;

import com.dela.brewery.models.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
@Builder
public class OrderValidationRequest implements Serializable {

    private static long serialVersionUID = -8152665195502943379L;

    private BeerOrderDto beerOrderDto;
}
