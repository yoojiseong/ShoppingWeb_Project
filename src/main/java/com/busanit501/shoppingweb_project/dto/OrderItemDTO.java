package com.busanit501.shoppingweb_project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long order_itemid;

    private Long productid; // camelCase


    private int quantity=1;

    @NotNull
    private BigDecimal price;

}
