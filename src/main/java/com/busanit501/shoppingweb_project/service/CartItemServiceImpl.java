package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.CartItem;
import com.busanit501.shoppingweb_project.dto.CartItemDTO;
import com.busanit501.shoppingweb_project.repository.CartItemRepository;
import com.busanit501.shoppingweb_project.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Override
    public CartItemDTO addToCart(CartItemDTO cartItemDTO) {
        log.info("CartItemServiceImpl에서 작업중 넘어온 DTO확인 : " + cartItemDTO);
    CartItem cartItem = CartItem.builder()
            .memberId(cartItemDTO.getMemberId())
            .product(productRepository.findByProductId(cartItemDTO.getProductId()))
            .quantity(cartItemDTO.getQuantity())
            .build();
        log.info("CartItemServiceImpl에서 작업중 변환된 Entity확인 : " + cartItem);
    cartItemRepository.save(cartItem);
        return cartItemDTO;
    }
}
