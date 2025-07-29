package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.*;
import com.busanit501.shoppingweb_project.dto.CartItemDTO;
import com.busanit501.shoppingweb_project.dto.MemberDTO;
import com.busanit501.shoppingweb_project.dto.OrderDTO;
import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.repository.CartItemRepository;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import com.busanit501.shoppingweb_project.repository.OrderRepository;
import com.busanit501.shoppingweb_project.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Builder
@RequiredArgsConstructor
@Transactional()
public class OrderServicImpl implements OrderService {

    private final ModelMapper modelMapper;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartRepository;
    private final MemberRepository memberRepository;

    @Override
    public OrderDTO PurchaseFromCart(Long memberId) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(memberId);
        Optional<Member> member = memberRepository.findById(memberId);
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어 있습니다.");
        }

        // 총합 계산

        BigDecimal total = BigDecimal.ZERO;

        // 주문 객체 생성
        Order order = Order.builder()
                .memberId(memberId)
                .orderDate(LocalDateTime.now())
                .status(true) // 또는 enum 사용 시 OrderStatus.ORDERED
                .address(memberDTO.getAddressId())
                .address_detail(memberDTO.getAddressLine())
                .receiverName(memberDTO.getUserName())
                .receiverPhone(memberDTO.getPhone())
                .build();

        for (CartItem cart : cartItems) {
            Product product = cart.getProduct();

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));
            total = total.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getProductId())
                    .quantity(cart.getQuantity())
                    .price(itemTotal)
                    .build();

            order.addOrderItem(orderItem); // 연관관계 설정
        }

        order.setTotalPrice(total.intValue()); // Order에 총합 저장

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // 장바구니 비우기
        cartItemRepository.deleteByMemberId(memberId);

        // DTO 매핑은 저장 후!
        return modelMapper.map(savedOrder, OrderDTO.class);
    }


    @Override
    public CartItemDTO AddCartItemFromProductDetail(CartItemDTO cartItemDTO) {
        CartItem cartItem = new ModelMapper().map(cartItemDTO, CartItem.class);

        cartItemRepository.save(cartItem);
        return cartItemDTO;
    }
}
