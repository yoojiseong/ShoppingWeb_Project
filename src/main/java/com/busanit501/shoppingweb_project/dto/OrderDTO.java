package com.busanit501.shoppingweb_project.dto;

import com.busanit501.shoppingweb_project.domain.Order;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderDTO {

    private Long orderId;
    // 나중에 어떤 order을 가져올지 알아야 해서

    private Long memberId;

    @CreatedDate
    @Column(name = "regDate", updatable = true)
    private LocalDateTime orderDate;

    private boolean status;
    private String address;
    private String addressDetail;
    private int totalPrice;
    private String receiverName;
    private String receiverPhone;

    private List<OrderItemDTO> orderItems;

    public static OrderDTO fromEntitySafe(Order order) {
        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .memberId(order.getMemberId())
                .orderDate(order.getOrderDate())  // null 가능, 프론트에서 처리
                .status(order.getStatus() != null ? order.getStatus() : false)
                .address(order.getAddress() != null ? order.getAddress() : "")
                .addressDetail(order.getAddressDetail() != null ? order.getAddressDetail() : "")
                .totalPrice(order.getTotalPrice())
                .receiverName(order.getReceiverName() != null ? order.getReceiverName() : "")
                .receiverPhone(order.getReceiverPhone() != null ? order.getReceiverPhone() : "")
                .orderItems(order.getOrderItems() != null ?
                        order.getOrderItems().stream()
                                .map(OrderItemDTO::fromEntitySafe) // OrderItemDTO에도 fromEntitySafe 필요
                                .toList()
                        : List.of())
                .build();
    }
}

