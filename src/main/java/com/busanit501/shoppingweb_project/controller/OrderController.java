package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.OrderDTO;
//import com.busanit501.shoppingweb_project.security.CustomUserDetails;
import com.busanit501.shoppingweb_project.security.MemberSecurityDTO;
import com.busanit501.shoppingweb_project.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> OrderToCart(
                                                @AuthenticationPrincipal MemberSecurityDTO userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long memberId = userDetails.getId();
        log.info("🛒 주문 요청 - memberId: {}", memberId);
        OrderDTO orderDTO = orderService.PurchaseFromCart(memberId);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrderHistory(@AuthenticationPrincipal MemberSecurityDTO userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long memberId = userDetails.getId();
        log.info("OrderController에서 myPage를 위해 작업중 넘어온 memberId : " + memberId);
        List<OrderDTO> orderHistory = orderService.getOrderHistoryByMemberId(memberId);
        orderHistory.forEach(orderDTO -> {
            log.info("가져온 주문 목록 : " + orderDTO);
        });
        return ResponseEntity.ok(orderHistory);
    }

    @GetMapping("/has-product")
    public ResponseEntity<Boolean> hasPurchasedProduct(
            @AuthenticationPrincipal MemberSecurityDTO userDetails,
            @RequestParam("productId") Long productId
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        log.info("OrderController에서 사용자가 상품을 구매했는지 확인중 : " + userDetails.getId());
        log.info("OrderController에서 사용자가 상품을 구매했는지 확인중 : " + productId);
        boolean hasPurchased = orderService.hasPurchasedProduct(userDetails.getId(), productId);
        return ResponseEntity.ok(hasPurchased);
    }

}
