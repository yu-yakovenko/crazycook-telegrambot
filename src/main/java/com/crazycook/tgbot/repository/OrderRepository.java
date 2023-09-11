package com.crazycook.tgbot.repository;

import com.crazycook.tgbot.entity.Order;
import com.crazycook.tgbot.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.boxes WHERE o.id = :id")
    Optional<Order> findByIdWithBoxes(@Param("id") Long id);
}