package com.dela.brewery.events;

import com.dela.brewery.models.beer_order.OrderValidationResultDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderValidationResponse implements Serializable {

    private static final long serialVersionUID = 4623629719564712992L;

    private OrderValidationResultDto orderValidationResultDto;
}

