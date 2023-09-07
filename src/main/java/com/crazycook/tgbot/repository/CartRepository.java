package com.crazycook.tgbot.repository;

import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.CartStatus;
import com.crazycook.tgbot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.boxes WHERE c.id = :id")
    Optional<Cart> findByIdWithBoxes(@Param("id") Long id);

    Cart findByCustomer(Customer customer);

    List<Cart> findByStatusNot(CartStatus status);

}