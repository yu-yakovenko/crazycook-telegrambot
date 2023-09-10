package com.crazycook.tgbot.command.delivery;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.service.PriceService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.courierButton;
import static com.crazycook.tgbot.bot.Buttons.selfPickupButton;
import static com.crazycook.tgbot.bot.Messages.DELIVERY_MESSAGE;

@AllArgsConstructor
public class ChooseDeliveryCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final PriceService priceService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        BigDecimal deliveryPrice = priceService.getDeliveryPrice();

        sendBotMessageService.sendMessage(chatId, String.format(DELIVERY_MESSAGE, deliveryPrice),
                List.of(List.of(courierButton(), selfPickupButton())));
    }
}
