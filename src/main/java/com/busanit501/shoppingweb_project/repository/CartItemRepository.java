package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("select b from CartItem b where b.memberId = :memberId")
    List<CartItem> findByMemberId(@Param("memberId")Long memberId);
}
