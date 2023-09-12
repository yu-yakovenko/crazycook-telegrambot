package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Price;
import com.crazycook.tgbot.repository.PriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Transactional
public class PriceService {
    private final PriceRepository priceRepository;
    private static final String DELIVERY_NAME = "delivery";

    public BigDecimal getBoxPrice(BoxSize boxSize) {
        return priceRepository.findByName(boxSize.name()).getValue();
    }

    public BigDecimal getDeliveryPrice() {
        return priceRepository.findByName(DELIVERY_NAME).getValue();
    }

    public Price getPricesForDelivery() {
        return priceRepository.findByName(DELIVERY_NAME);
    }

    public List<Price> getPricesForBoxes() {
        List<String> boxNames = Arrays.stream(BoxSize.values()).map(Enum::name).collect(Collectors.toList());
        return priceRepository.findAll().stream().filter(p -> boxNames.contains(p.getName())).collect(Collectors.toList());
    }

    public Price findById(Long priceId) {
        Price p = priceRepository.getById(priceId);
        p.getValue(); // the only way to avoid could not initialize proxy - no Session
        return p;
    }

    public Price save(Price price) {
        return priceRepository.save(price);
    }
}
