package com.dela.brewery.models.beer_order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class OrderValidationResultDto {
    private UUID id;
    private boolean isValid;
}
