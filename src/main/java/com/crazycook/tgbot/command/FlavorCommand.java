package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.crazycook.tgbot.bot.Buttons.createOrderButton;
import static com.crazycook.tgbot.bot.Buttons.startButton;
import static com.crazycook.tgbot.Utils.getChatId;

public class FlavorCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;

    public final static String FLAVOR_MESSAGE = "<b>Зараз в наявності є такі смаки: </b>\n";

    public FlavorCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);

        //todo get flavors from DB
        List<String> flavors = List.of("Манго-маракуйя", "Шоколад-банан", "Космополітан", "Полуничне мохіто");
        String message = flavors
                .stream()
                .reduce(FLAVOR_MESSAGE, (partialString, element) -> partialString + "\uD83D\uDD38 " + element + "; \n");

        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(startButton());
        buttonRow.add(createOrderButton());

        sendBotMessageService.sendMessage(chatId, message, List.of(buttonRow));
    }
}
