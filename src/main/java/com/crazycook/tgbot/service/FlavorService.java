package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.repository.FlavorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class FlavorService {
    private final FlavorRepository flavorRepository;

    public Set<String> getAllInStockNames() {
        return flavorRepository.getByIsInStock(true)
                .stream()
                .map(Flavor::getName)
                .collect(Collectors.toSet());
    }

    public Set<Flavor> getAllInStock() {
        return flavorRepository.getByIsInStock(true);
    }

    public Set<Flavor> getAllNotInStock() {
        return flavorRepository.getByIsInStock(false);
    }

    public Flavor getById(String flavorId) {
        Flavor flavor = flavorRepository.getById(Long.parseLong(flavorId));
        flavor.getName(); // the only way to avoid could not initialize proxy - no Session
        return flavor;
    }

    public Flavor save(Flavor flavor) {
        return flavorRepository.save(flavor);
    }
}
