package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.dto.CartItemDTO;
import com.busanit501.shoppingweb_project.dto.OrderDTO;
import com.busanit501.shoppingweb_project.dto.ProductDTO;

import java.util.List;

public interface OrderService {
    OrderDTO PurchaseFromCart(Long memberId);
    CartItemDTO AddCartItemFromProductDetail(CartItemDTO cartItemDTO);
    List<OrderDTO> getOrderHistoryByMemberId(Long memberId);
    boolean hasPurchasedProduct(Long memberId, Long productId);
}
