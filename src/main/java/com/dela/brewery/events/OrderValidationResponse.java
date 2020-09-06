package com.dela.brewery.events;

import com.dela.brewery.models.beer_order.OrderValidationResultDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class OrderValidationResponse implements Serializable {

    private static final long serialVersionUID = 4623629719564712992L;

    private final OrderValidationResultDto orderValidationResultDto;
}

