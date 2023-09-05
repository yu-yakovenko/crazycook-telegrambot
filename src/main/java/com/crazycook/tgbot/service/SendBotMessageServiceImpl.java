package com.crazycook.tgbot.service;

import com.crazycook.tgbot.bot.CrazyCookTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private final CrazyCookTelegramBot crazyCookTelegramBot;
    private SendMessage sendMessage = new SendMessage();
    public static final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();


    @Autowired
    public SendBotMessageServiceImpl(CrazyCookTelegramBot crazyCookTelegramBot) {
        this.crazyCookTelegramBot = crazyCookTelegramBot;
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        sendMessage(chatId, message, Collections.emptyList());
    }

    @Override
    public void sendMessage(Long chatId, List<String> messages) {
        if (isEmpty(messages)) return;

        messages.forEach(m -> sendMessage(chatId, m));
    }

    @Override
    public void sendMessage(Long chatId, String message, List<List<InlineKeyboardButton>> buttons) {
        if (isBlank(message)) return;

        sendMessage.setChatId(chatId.toString());
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        keyboardMarkup.setKeyboard(buttons);
        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            crazyCookTelegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            //todo add logging to the project.
            e.printStackTrace();
        }
    }
}
