package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.domain.CartItem;
import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.dto.CartItemDTO;
import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.repository.CartItemRepository;
import com.busanit501.shoppingweb_project.repository.ProductRepository;
import com.busanit501.shoppingweb_project.service.CartItemService;
import com.busanit501.shoppingweb_project.security.CustomUserDetails;
import com.busanit501.shoppingweb_project.service.OrderService;
import com.busanit501.shoppingweb_project.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Log4j2
public class CartController {
    private final OrderService orderService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;


    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItems(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        log.info("CartController에서 작업중 memberId : " + memberId);
        return ResponseEntity.ok(cartItemService.getCartItemsByMemberId(memberId));
    }


    @PostMapping
    public ResponseEntity<CartItemDTO> addToCart(@RequestBody CartItemDTO dto,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("화면에서 받아온 dto확인중 : " +dto);
        Long memberId = userDetails.getMemberId();
        dto.setMemberId(memberId);

        CartItemDTO saved = cartItemService.addToCart(dto);
        return ResponseEntity.ok(saved);
    }
    @PatchMapping("/{productId}")
    public ResponseEntity<CartItemDTO> updateQuantity(@PathVariable Long productId,
                                                      @RequestBody CartItemDTO change,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        CartItemDTO updated = cartItemService.updateQuantity( productId, change.getQuantity());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long productId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        cartItemService.removeFromCart(userDetails.getMemberId(), productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        cartItemService.clearCart(userDetails.getMemberId());
        return ResponseEntity.ok().build();
    }
}
