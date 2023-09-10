package com.crazycook.tgbot.repository;

import com.crazycook.tgbot.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    Price findByName(String name);
}
