package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.cartInProgressButtons;

@AllArgsConstructor
public class ShowCartCommand implements CrazyCookTGCommand {
    public static final String MESSAGE = "В твоїй корзині зараз: \n";

    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    @Override
    @Transactional
    public void execute(Update update) {
        Long chatId = getChatId(update);
        Cart cart = cartService.findCart(chatId);

        String message = MESSAGE;
        Set<Box> boxes = cartService.getBoxesForCart(cart.getId());

        if (boxes.isEmpty()) {
            if (cart.getSNumber() > 0) {
                message += "- " + cart.getSNumber() + " S пустих боксів \n";
            }
            if (cart.getMNumber() > 0) {
                message += "- " + cart.getMNumber() + " M пустих боксів \n";
            }
            if (cart.getLNumber() > 0) {
                message += "- " + cart.getLNumber() + " L пустих боксів \n";
            }
        }
        sendBotMessageService.sendMessage(getChatId(update), message, cartInProgressButtons());
    }
}
