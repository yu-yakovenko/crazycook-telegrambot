package com.crazycook.tgbot.command.cart;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.customerMenuButtons;
import static com.crazycook.tgbot.bot.Messages.CART_WAS_REFRESH;

@AllArgsConstructor
public class RefreshCartCommand implements CrazyCookTGCommand {

    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    @Override
    public void execute(Update update) {
        Long customerChatId = getChatId(update);
        String customerUsername = getUserName(update);
        Cart cart = cartService.createOrFind(customerChatId, customerUsername);

        cartService.delete(cart);

        sendBotMessageService.sendMessage(customerChatId, CART_WAS_REFRESH, customerMenuButtons());
    }
}
