package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.repository.FlavorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FlavorService {
    private final FlavorRepository flavorRepository;

    public Set<String> getAllInStockNames() {
        return flavorRepository.getByIsInStock(true)
                .stream()
                .map(Flavor::getName)
                .collect(Collectors.toSet());
    }
}
