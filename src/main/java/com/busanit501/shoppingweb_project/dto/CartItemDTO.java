package com.busanit501.shoppingweb_project.dto;

import com.busanit501.shoppingweb_project.domain.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long cartItemId;

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private int quantity;

    private ProductDTO productDTO;

    public static CartItemDTO fromEntity(CartItem cartItem , ProductDTO productDTO) {
        return CartItemDTO.builder()
                .cartItemId(cartItem.getCartItemId())
                .memberId(cartItem.getMemberId())
                .productId(cartItem.getProduct().getProductId())
                .quantity(cartItem.getQuantity())
                .productDTO(productDTO)
                .build();
    }

}