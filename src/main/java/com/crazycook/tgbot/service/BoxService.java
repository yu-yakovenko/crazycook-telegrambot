package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.FlavorQuantity;
import com.crazycook.tgbot.repository.BoxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class BoxService {

    private final BoxRepository boxRepository;

    public Box save(Box box) {
        return boxRepository.save(box);
    }


    public List<FlavorQuantity> getFlavorQuantitiesForBox(Long id) {
        Optional<Box> optBox = boxRepository.findByIdWithFlavorQuantities(id);
        if (optBox.isPresent()) {
            Box box = optBox.get();
            return box.getFlavorQuantities();
        } else {
            return Collections.emptyList();
        }
    }


    public String flavorQuantitiesToString(Box box) {
        if (box.getIsMix()) {
            return "";
        }
        List<FlavorQuantity> flavorQuantity = getFlavorQuantitiesForBox(box.getId());
        if (flavorQuantity.isEmpty()) {
            return "";
        }
        StringBuilder message = new StringBuilder("<b>Бокс " + box.getBoxSize() + ", що міcтить: </b>\n");
        for (FlavorQuantity fq : flavorQuantity) {
            message.append("    ").append(fq.toString());
        }
        return message.toString();
    }

}
