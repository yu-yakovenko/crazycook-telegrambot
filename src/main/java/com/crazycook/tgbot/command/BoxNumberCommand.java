package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.cartInProgressButtons;
import static com.crazycook.tgbot.bot.Messages.FLOATING_NUMBER_REACTION;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.NEGATIVE_NUMBER_REACTION;
import static com.crazycook.tgbot.bot.Messages.TO_LONG_INTEGER_NUMBER_REACTION;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_APPROVE;

@AllArgsConstructor
public class BoxNumberCommand implements CrazyCookTGCommand {
    public static final String MESSAGE_FORMAT = "Супер, ми додали %s %s боксів до твого кошика.";
    public static String regExIntNumber = "[+-]?[0-9]+";

    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;
    private final AdminService adminService;

    @Override
    public void execute(Update update) {
        String message = getMessage(update);
        if (message.matches(regExIntNumber)) {
            try {
                Integer.parseInt(message);
                normalBehaviour(update);
            } catch (NumberFormatException ignored) {
                sendBotMessageService.sendMessage(getChatId(update), TO_LONG_INTEGER_NUMBER_REACTION);
            }
        } else {

            if (message.contains(",")) {
                message = message.replace(',', '.');
            }

            try {
                Double.parseDouble(message);
                sendBotMessageService.sendMessage(getChatId(update), FLOATING_NUMBER_REACTION);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void normalBehaviour(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        int incomeNumber = Integer.parseInt(getMessage(update));
        if (incomeNumber < 0) {
            negativeBehaviour(chatId);
            return;
        }

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
        sendBotMessageService.sendMessage(chatId, String.format(MESSAGE_FORMAT, incomeNumber, boxSize), cartInProgressButtons());
    }

    private void negativeBehaviour(Long chatId) {
        StringBuilder sb = new StringBuilder(NEGATIVE_NUMBER_REACTION + LINE_END);
        adminService.getAdminUsernames().forEach(admin -> sb.append("@").append(admin).append(LINE_END));
        sendBotMessageService.sendMessage(chatId, sb.toString());
    }
}
