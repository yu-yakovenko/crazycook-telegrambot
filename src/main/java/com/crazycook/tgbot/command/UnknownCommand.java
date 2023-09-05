package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.mainMenuButtons;

public class UnknownCommand implements CrazyCookTGCommand {
    public static final String UNKNOWN_MESSAGE = "Вибач, я не розумію, твоє повідомлення \uD83E\uDD14. Ось головне меню, обери що тебе цікавить:";

    private final SendBotMessageService sendBotMessageService;

    public UnknownCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        // todo check if user has a cart in box_number_waiting status
        sendBotMessageService.sendMessage(getChatId(update), UNKNOWN_MESSAGE, mainMenuButtons());
    }
}
