package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.service.PriceService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.createOrderButton;
import static com.crazycook.tgbot.bot.Buttons.startButton;
import static com.crazycook.tgbot.bot.Messages.PRICE_MESSAGE;

@AllArgsConstructor
public class PriceCommand implements CrazyCookTGCommand {

    private final SendBotMessageService sendBotMessageService;
    private final PriceService priceService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);

        BigDecimal sPrice = priceService.getBoxPrice(BoxSize.S);
        BigDecimal mPrice = priceService.getBoxPrice(BoxSize.M);
        BigDecimal lPrice = priceService.getBoxPrice(BoxSize.L);

        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(startButton());
        buttonRow.add(createOrderButton());

        sendBotMessageService.sendMessage(chatId, String.format(PRICE_MESSAGE, sPrice, mPrice, lPrice), List.of(buttonRow));
    }
}
