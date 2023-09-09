package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.cartInProgressButtons;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_APPROVE;

@AllArgsConstructor
public class BoxNumberCommand implements CrazyCookTGCommand {
    public static final String MESSAGE_FORMAT = "Супер, ми додали %s %s боксів до твого кошика.";

    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        int incomeNumber = Integer.parseInt(getMessage(update));
        Cart cart = cartService.createOrFind(chatId, username);
        String boxSize;
        switch (cart.getStatus()) {
            case WAITING_FOR_S_NUMBER -> {
                cart.setSNumber(cart.getSNumber() + incomeNumber);
                boxSize = "S";
            }
            case WAITING_FOR_M_NUMBER -> {
                cart.setMNumber(cart.getMNumber() + incomeNumber);
                boxSize = "M";
            }
            case WAITING_FOR_L_NUMBER -> {
                cart.setLNumber(cart.getLNumber() + incomeNumber);
                boxSize = "L";
            }
            default -> throw new IllegalStateException("Unexpected value: " + cart.getStatus());
        }
        cart.setStatus(WAITING_FOR_APPROVE);
        cartService.save(cart);
        sendBotMessageService.sendMessage(getChatId(update), String.format(MESSAGE_FORMAT, incomeNumber, boxSize), cartInProgressButtons());
    }
}
