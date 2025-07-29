package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.OrderDTO;
import com.busanit501.shoppingweb_project.security.CustomUserDetails;
import com.busanit501.shoppingweb_project.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> OrderToCart(
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        log.info("🛒 주문 요청 - memberId: {}", memberId);
        OrderDTO orderDTO = orderService.PurchaseFromCart(memberId);
        return ResponseEntity.ok(orderDTO);
    }
}
