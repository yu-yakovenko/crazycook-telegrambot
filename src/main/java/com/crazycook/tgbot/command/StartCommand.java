package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.mainMenuButtons;

@AllArgsConstructor
public class StartCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CustomerService customerService;

    public final static String START_MESSAGE = "Привіт \uD83D\uDC4B, раді тебе вітати в нашому чат-боті для замовлення макарон. Ось головне меню, обери, що тебе цікавить: ";


    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        customerService.createOrFind(chatId, username);

        sendBotMessageService.sendMessage(chatId, START_MESSAGE, mainMenuButtons());
    }
}
