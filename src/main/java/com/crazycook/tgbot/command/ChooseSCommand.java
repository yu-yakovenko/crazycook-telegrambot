package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_S_NUMBER;

@AllArgsConstructor
public class ChooseSCommand implements CrazyCookTGCommand {

    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    public final static String CHOOSE_S_MESSAGE = "Напиши цифрою скільки всього S боксів ти хочеш замовити? \n";

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        Cart cart = cartService.createOrFind(chatId);
        cart.setStatus(WAITING_FOR_S_NUMBER);
        cartService.save(cart);
        sendBotMessageService.sendMessage(chatId, CHOOSE_S_MESSAGE);
    }
}
