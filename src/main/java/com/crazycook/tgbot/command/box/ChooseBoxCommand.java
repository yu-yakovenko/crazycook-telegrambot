package com.crazycook.tgbot.command.box;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.boxSizeButtons;
import static com.crazycook.tgbot.bot.Messages.CHOOSE_BOX;

public class ChooseBoxCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;

    public ChooseBoxCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);

        sendBotMessageService.hidePreviousButtons(update, chatId);
        sendBotMessageService.sendMessage(chatId, CHOOSE_BOX, boxSizeButtons());
    }
}
