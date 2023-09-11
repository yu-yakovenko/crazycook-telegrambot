package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.mainMenuButtons;
import static com.crazycook.tgbot.bot.Messages.START_MESSAGE;

@AllArgsConstructor
public class StartCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CustomerService customerService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        customerService.createOrFind(chatId, username);

        sendBotMessageService.sendMessage(chatId, START_MESSAGE, mainMenuButtons());
    }
}
