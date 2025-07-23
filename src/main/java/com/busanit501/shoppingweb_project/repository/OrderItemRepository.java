package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    @Query("select b from OrderItem b where b.orderItemId = :orderItemId")
    OrderItem findByOrderItemId(@Param("orderItemId") Long orderItemId);
}
