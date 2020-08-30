package guru.sfg.beer.order.service.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class BeerDto {

    @JsonProperty("beerId")
    private UUID id;

    private String beerName;
    private String upc;
    private BigDecimal price;
    private BeerStyleEnum beerStyle;
}
