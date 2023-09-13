package com.crazycook.tgbot.command.flavor;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Set;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.chooseBoxButton;
import static com.crazycook.tgbot.bot.Buttons.deliveryButton;
import static com.crazycook.tgbot.bot.Buttons.priceButton;
import static com.crazycook.tgbot.bot.Messages.FLAVOR_MESSAGE;

@AllArgsConstructor
public class FlavorInStockCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final FlavorService flavorService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);

        Set<String> flavors = flavorService.getAllInStockNames();
        String message = flavors
                .stream()
                .reduce(FLAVOR_MESSAGE, (partialString, element) -> partialString + "\uD83D\uDD38 " + element + "; \n");

        List<InlineKeyboardButton> buttonRow1 = List.of(priceButton(), chooseBoxButton());
        List<InlineKeyboardButton> buttonRow2 = List.of(deliveryButton());

        sendBotMessageService.sendMessage(chatId, message, List.of(buttonRow1, buttonRow2));
    }
}
