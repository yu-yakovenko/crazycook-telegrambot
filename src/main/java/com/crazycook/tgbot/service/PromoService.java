package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Promo;
import com.crazycook.tgbot.repository.PromoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class PromoService {
    private final PromoRepository promoRepository;

    public Optional<Promo> findByName(String name) {
        return promoRepository.findByName(name.toLowerCase());
    }

    public boolean isNotExpired(Promo promo) {
        return LocalDate.now().isBefore(promo.getExpiringDate());
    }
}
