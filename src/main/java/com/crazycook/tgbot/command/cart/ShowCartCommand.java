package com.crazycook.tgbot.command.cart;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.addMoreButton;
import static com.crazycook.tgbot.bot.Buttons.chooseDeliveryButton;
import static com.crazycook.tgbot.bot.Buttons.chooseFlavorsLongButton;
import static com.crazycook.tgbot.bot.Buttons.mixFlavorForAllButton;
import static com.crazycook.tgbot.bot.Buttons.promoCodeButton;
import static com.crazycook.tgbot.bot.Buttons.refreshCartButton;
import static com.crazycook.tgbot.bot.Messages.CART_CONFIRM_REQUIREMENT;
import static com.crazycook.tgbot.bot.Messages.DELIVERY_NEEDED;
import static com.crazycook.tgbot.bot.Messages.IN_YOUR_CART;
import static com.crazycook.tgbot.bot.Messages.LINE_END;

@AllArgsConstructor
public class ShowCartCommand implements CrazyCookTGCommand {

    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    @Override
    @Transactional
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        Cart cart = cartService.findCart(chatId, username);

        StringBuilder message = new StringBuilder(IN_YOUR_CART);
        message.append(cartService.cartSummery(cart));

        boolean emptyBoxes = cartService.containsEmptyBoxes(cart);
        boolean emptyCart = cart.getSNumber() + cart.getMNumber() + cart.getLNumber() == 0;

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        if (emptyBoxes) {
            buttons.add(List.of(addMoreButton(), refreshCartButton()));
            buttons.add(List.of(chooseFlavorsLongButton()));
            buttons.add(List.of(mixFlavorForAllButton()));
            message.append(LINE_END + LINE_END + CART_CONFIRM_REQUIREMENT);
        } else if (!emptyCart) {
            buttons.add(List.of(addMoreButton(), chooseDeliveryButton()));
            buttons.add(List.of(promoCodeButton(), refreshCartButton()));
            message.append(LINE_END + LINE_END + DELIVERY_NEEDED);
        } else {
            buttons.add(List.of(addMoreButton()));
            message.append(LINE_END + LINE_END + CART_CONFIRM_REQUIREMENT);
        }


        sendBotMessageService.sendMessage(getChatId(update), message.toString(), buttons);
    }
}
