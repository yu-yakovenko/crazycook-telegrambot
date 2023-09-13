package com.crazycook.tgbot.command.box;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.boxSizeButtons;
import static com.crazycook.tgbot.bot.Buttons.chooseFlavorsButton;
import static com.crazycook.tgbot.bot.Messages.BOX_ADDED;
import static com.crazycook.tgbot.bot.Messages.IN_YOUR_CART;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.ONE_MORE_BOX_ADDED;
import static com.crazycook.tgbot.entity.CartStatus.IN_PROGRESS;

@AllArgsConstructor
public class AddBoxCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        String callback = getMessage(update);

        Cart cart = cartService.createOrFind(chatId, username);

        BoxSize size = BoxSize.valueOf(callback.split(" ")[1].trim());
        switch (size) {
            case S -> cart.setSNumber(cart.getSNumber() + 1);
            case M -> cart.setMNumber(cart.getMNumber() + 1);
            case L -> cart.setLNumber(cart.getLNumber() + 1);
            default -> throw new IllegalStateException("Unexpected value: " + cart.getStatus());
        }

        List<List<InlineKeyboardButton>> buttons = List.of(boxSizeButtons(),
                List.of(chooseFlavorsButton()));

        cart.setStatus(IN_PROGRESS);
        cartService.save(cart);
        sendBotMessageService.editMessage(update.getCallbackQuery().getMessage().getMessageId(),
                chatId,
                message(cart, size),
                buttons);
    }

    private String message(Cart cart, BoxSize size) {
        String message = "";
        if (cart.getSNumber() + cart.getMNumber() + cart.getLNumber() == 1) {
            message += String.format(BOX_ADDED, size.name());
        } else {
            message += String.format(ONE_MORE_BOX_ADDED, size.name());
        }
        return message + LINE_END + LINE_END + IN_YOUR_CART + cartService.cartBoxesToString(cart);
    }
}
