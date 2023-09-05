package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.lButton;
import static com.crazycook.tgbot.bot.Buttons.mButton;
import static com.crazycook.tgbot.bot.Buttons.sButton;

public class ChooseBoxCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;

    public ChooseBoxCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    public final static String CHOOSE_BOX_MESSAGE = "Обери розмір боксу: \n";

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);

        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(sButton());
        buttonRow.add(mButton());
        buttonRow.add(lButton());

        sendBotMessageService.sendMessage(chatId, CHOOSE_BOX_MESSAGE, List.of(buttonRow));
    }
}
