package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.FlavorQuantity;
import com.crazycook.tgbot.repository.BoxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.crazycook.tgbot.bot.Messages.BOLD_END;
import static com.crazycook.tgbot.bot.Messages.BOLD_START;
import static com.crazycook.tgbot.bot.Messages.EMPTY_BOX;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.YELLOW_DIAMOND;

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
        StringBuilder message = new StringBuilder(YELLOW_DIAMOND + BOLD_START + "Бокс " + box.getBoxSize() + ", що міcтить:" + BOLD_END + LINE_END);
        for (FlavorQuantity fq : flavorQuantity) {
            message.append("    ").append(fq.toString());
        }
        return message.toString();
    }


    public String messageForEmptyBoxes(Cart cart, int filledSNumber, int filledMNumber, int filledLNumber) {
        return messageForEmptyBox(filledSNumber, cart.getSNumber(), BoxSize.S)
                + messageForEmptyBox(filledMNumber, cart.getMNumber(), BoxSize.M)
                + messageForEmptyBox(filledLNumber, cart.getLNumber(), BoxSize.L);
    }

    private String messageForEmptyBox(int filledNumber, int thisSizeNumber, BoxSize size) {
        String message = "";
        if (thisSizeNumber > filledNumber) {
            message = (String.format(EMPTY_BOX, thisSizeNumber - filledNumber, size));
        }
        return message;
    }

}
