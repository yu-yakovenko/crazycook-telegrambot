package com.crazycook.tgbot.command.delivery;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.service.PriceService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.chooseBoxButton;
import static com.crazycook.tgbot.bot.Buttons.flavorsButton;
import static com.crazycook.tgbot.bot.Buttons.priceButton;
import static com.crazycook.tgbot.bot.Messages.DELIVERY_MESSAGE;

@AllArgsConstructor
public class DeliveryCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final PriceService priceService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);

        List<InlineKeyboardButton> buttonRow1 = List.of(priceButton(), chooseBoxButton());
        List<InlineKeyboardButton> buttonRow2 = List.of(flavorsButton());

        BigDecimal deliveryPrice = priceService.getDeliveryPrice();

        sendBotMessageService.sendMessage(chatId, String.format(DELIVERY_MESSAGE, deliveryPrice), List.of(buttonRow1, buttonRow2));
    }
}
