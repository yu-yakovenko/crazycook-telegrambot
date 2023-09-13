package com.crazycook.tgbot.command.comment;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
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
import static com.crazycook.tgbot.bot.Buttons.cartCompleteButtons;
import static com.crazycook.tgbot.bot.Messages.COMMENT_ADDED;
import static com.crazycook.tgbot.entity.CartStatus.IN_PROGRESS;

@AllArgsConstructor
public class CommentCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    @Override
    public void execute(Update update) {
        Long customerChatId = getChatId(update);
        String customerUsername = getUserName(update);
        String message = getMessage(update);
        Cart cart = cartService.findCart(customerChatId, customerUsername);
        cart.setComment(message);
        cart.setStatus(IN_PROGRESS);
        cartService.save(cart);
        List<List<InlineKeyboardButton>> buttons = cartCompleteButtons(cartService.readyForComplete(cart));

        sendBotMessageService.sendMessage(customerChatId, COMMENT_ADDED, buttons);
    }
}