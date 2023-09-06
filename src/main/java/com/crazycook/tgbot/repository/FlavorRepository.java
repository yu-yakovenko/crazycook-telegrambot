package com.crazycook.tgbot.repository;

import com.crazycook.tgbot.entity.Flavor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FlavorRepository extends JpaRepository<Flavor, Long> {

    Set<Flavor> getByIsInStock(Boolean isInStock);

}
