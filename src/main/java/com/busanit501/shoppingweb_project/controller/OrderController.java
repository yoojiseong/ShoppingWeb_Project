package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    public String checkout() {
        try {
            // 누구나 주문 가능하도록 memberId를 임시로 지정
            Long testMemberId = 1L; // ★ 임시 memberId (나중에 로그인 정보로 교체 가능)

            log.info("임시 사용자 주문 처리 시작 - memberId: {}", testMemberId);

            orderService.PurchaseFromCart(testMemberId);

            return "redirect:/index"; // 주문 완료 페이지로 이동
        } catch (Exception e) {
            log.error("주문 처리 중 오류 발생", e);
            return "redirect:/cart?error=true";
        }
    }
}
