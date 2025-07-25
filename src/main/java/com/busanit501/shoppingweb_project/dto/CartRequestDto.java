package com.busanit501.shoppingweb_project.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequestDto {

    // 어떤 상품을 담을지 알려주는 ID. 절대 null일 수 없다.
    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long productId;

    // 몇 개를 담을지 알려주는 수량. 최소 1개 이상이어야 한다.
    @Min(value = 1, message = "최소 1개 이상 담아주세요.")
    private int quantity;
}