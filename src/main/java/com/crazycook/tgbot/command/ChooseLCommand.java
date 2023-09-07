package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_L_NUMBER;

public class ChooseLCommand implements CrazyCookTGCommand {

    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    public final static String CHOOSE_L_MESSAGE = "Напиши цифрою скільки всього L боксів ти хочеш замовити? \n";

    public ChooseLCommand(SendBotMessageService sendBotMessageService, CartService cartService) {
        this.sendBotMessageService = sendBotMessageService;
        this.cartService = cartService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);

        Cart cart = cartService.createOrFind(chatId, username);
        cart.setStatus(WAITING_FOR_L_NUMBER);
        cartService.save(cart);
        sendBotMessageService.sendMessage(chatId, CHOOSE_L_MESSAGE);
    }
}
