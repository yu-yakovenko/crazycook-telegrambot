package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.addMoreButton;
import static com.crazycook.tgbot.bot.Buttons.chooseDeliveryButton;
import static com.crazycook.tgbot.bot.Buttons.chooseFlavorsButton;

@AllArgsConstructor
public class ShowCartCommand implements CrazyCookTGCommand {
    public static final String MESSAGE = "В твоїй корзині зараз: \n";

    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;
    private final BoxService boxService;

    @Override
    @Transactional
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        Cart cart = cartService.findCart(chatId, username);

        StringBuilder message = new StringBuilder(MESSAGE);
        Set<Box> boxes = cartService.getBoxesForCart(cart.getId());

        int filledSNumber = getBoxNumber(boxes, BoxSize.S);
        int filledMNumber = getBoxNumber(boxes, BoxSize.M);
        int filledLNumber = getBoxNumber(boxes, BoxSize.L);

        boolean emptyBoxes = false;
        if (cart.getSNumber() > filledSNumber) {
            message.append("    ").append(cart.getSNumber() - filledSNumber).append(" <b>пустих S</b> боксів \n");
            emptyBoxes = true;
        }
        if (cart.getMNumber() > filledMNumber) {
            message.append("    ").append(cart.getMNumber() - filledMNumber).append(" <b>пустих</b> M боксів \n");
            emptyBoxes = true;
        }
        if (cart.getLNumber() > filledLNumber) {
            message.append("    ").append(cart.getLNumber() - filledLNumber).append(" <b>пустих</b> L боксів \n");
            emptyBoxes = true;
        }

        List<String> flavorDescription = boxes.stream().map(boxService::flavorQuantitiesToString).collect(Collectors.toList());

        for (String s : flavorDescription) {
            message.append(s);
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(addMoreButton()));
        if (emptyBoxes) {
            buttons.add(List.of(chooseFlavorsButton()));
        } else {
            buttons.add(List.of(chooseDeliveryButton()));
        }

        sendBotMessageService.sendMessage(getChatId(update), message.toString(), buttons);
    }

    private int getBoxNumber(Set<Box> boxes, BoxSize size) {
        return (int) boxes.stream().filter(b -> size.equals(b.getBoxSize())).count();
    }
}
