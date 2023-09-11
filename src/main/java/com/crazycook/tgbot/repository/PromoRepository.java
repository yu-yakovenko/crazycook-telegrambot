package com.crazycook.tgbot.repository;

import com.crazycook.tgbot.entity.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromoRepository extends JpaRepository<Promo, Long> {

    Optional<Promo> findByName(String name);
}
