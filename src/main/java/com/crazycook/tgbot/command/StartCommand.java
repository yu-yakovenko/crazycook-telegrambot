package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.bot.Buttons.mainMenuButtons;
import static com.crazycook.tgbot.Utils.getChatId;

public class StartCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;

    public final static String START_MESSAGE = "Привіт \uD83D\uDC4B, раді тебе вітати в нашому чат-боті для замовлення макарон. Ось головне меню, обери, що тебе цікавить: ";

    public StartCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);

        //todo identify user

        sendBotMessageService.sendMessage(chatId, START_MESSAGE, mainMenuButtons());
    }
}
