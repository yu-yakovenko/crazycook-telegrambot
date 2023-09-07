package com.crazycook.tgbot.repository;

import com.crazycook.tgbot.entity.FlavorQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlavorQuantityRepository extends JpaRepository<FlavorQuantity, Long> {

}