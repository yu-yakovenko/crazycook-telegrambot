package com.crazycook.tgbot.repository;

import com.crazycook.tgbot.entity.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {

    @Query("SELECT b FROM Box b LEFT JOIN FETCH b.flavorQuantities WHERE b.id = :id")
    Optional<Box> findByIdWithFlavorQuantities(@Param("id") Long id);

}