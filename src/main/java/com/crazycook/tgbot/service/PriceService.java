package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.repository.PriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


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

}
