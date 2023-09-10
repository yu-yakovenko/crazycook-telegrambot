package com.crazycook.tgbot.command.delivery;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.CartStatus;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.requestContactButton;
import static com.crazycook.tgbot.bot.Messages.ADDRESS_SAVED;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.REQUEST_CONTACT;

@AllArgsConstructor
public class AddressCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        String address = getMessage(update);

        Cart cart = cartService.createOrFind(chatId, username);
        cart.setAddress(address);
        cart.setStatus(CartStatus.IN_PROGRESS);
        cartService.save(cart);

        String message = ADDRESS_SAVED + LINE_END + REQUEST_CONTACT;

        sendBotMessageService.requestContact(chatId, message, requestContactButton());
    }
}
