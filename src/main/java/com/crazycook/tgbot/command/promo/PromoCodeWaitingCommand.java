package com.crazycook.tgbot.command.promo;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_PROMO;

@AllArgsConstructor
public class PromoCodeWaitingCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    @Override
    public void execute(Update update) {
        Long customerChatId = getChatId(update);
        String customerUsername = getUserName(update);

        Cart cart = cartService.findCart(customerChatId, customerUsername);
        cart.setStatus(WAITING_FOR_PROMO);
        cartService.save(cart);

        sendBotMessageService.sendMessage(customerChatId, "Введи промокод");
    }
}
