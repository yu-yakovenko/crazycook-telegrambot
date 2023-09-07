package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.FlavorQuantity;
import com.crazycook.tgbot.repository.FlavorQuantityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class FlavorQuantityService {

    private final FlavorQuantityRepository flavorQuantityRepository;


    public FlavorQuantity save(FlavorQuantity fq) {
        return flavorQuantityRepository.save(fq);
    }
}
