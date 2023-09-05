package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Buttons.mainMenuButtons;
import static com.crazycook.tgbot.Utils.getChatId;

public class UnknownCommand implements CrazyCookTGCommand {
    public static final String UNKNOWN_MESSAGE = "Вибач, я не розумію, твоє повідомлення \uD83D\uDE1F. Ось головне меню, обери що тебе цікавить:";

    private final SendBotMessageService sendBotMessageService;

    public UnknownCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(getChatId(update), UNKNOWN_MESSAGE, mainMenuButtons());
    }
}
