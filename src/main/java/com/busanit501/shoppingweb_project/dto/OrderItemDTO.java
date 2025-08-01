package com.busanit501.shoppingweb_project.dto;

import com.busanit501.shoppingweb_project.domain.OrderItem;
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

    private String productName;

    @Builder.Default
    private int quantity=1;

    @NotNull
    private BigDecimal price;

    public static OrderItemDTO fromEntitySafe(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .productid(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

}
