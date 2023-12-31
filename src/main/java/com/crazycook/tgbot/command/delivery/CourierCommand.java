package com.crazycook.tgbot.command.delivery;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.CartStatus;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Messages.INPUT_ADDRESS;
import static com.crazycook.tgbot.entity.DeliveryMethod.COURIER;

@AllArgsConstructor
public class CourierCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);

        Cart cart = cartService.createOrFind(chatId, username);
        cart.setDeliveryMethod(COURIER);
        cart.setStatus(CartStatus.WAITING_FOR_ADDRESS);
        cartService.save(cart);

        sendBotMessageService.sendMessage(chatId, INPUT_ADDRESS);
    }
}
