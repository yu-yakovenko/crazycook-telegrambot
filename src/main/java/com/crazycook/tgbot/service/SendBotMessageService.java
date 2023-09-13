package com.crazycook.tgbot.service;

import com.crazycook.tgbot.bot.CrazyCookTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class SendBotMessageService {

    private final CrazyCookTelegramBot crazyCookTelegramBot;
    private EditMessageText editMessageText = new EditMessageText();
    private SendMessage sendMessage = new SendMessage();
    public static final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
    public static final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();


    @Autowired
    public SendBotMessageService(CrazyCookTelegramBot crazyCookTelegramBot) {
        this.crazyCookTelegramBot = crazyCookTelegramBot;
    }

    public void sendMessage(Long chatId, String message) {
        sendMessage(chatId, message, Collections.emptyList());
    }

    public void sendMessage(Long chatId, List<String> messages) {
        if (isEmpty(messages)) return;

        messages.forEach(m -> sendMessage(chatId, m));
    }

    public void sendMessage(Long chatId, String message, List<List<InlineKeyboardButton>> buttons) {
        keyboardMarkup.setKeyboard(buttons);
        sendMessage(chatId, message, keyboardMarkup);
    }

    public void sendMessage(Long chatId, String message, ReplyKeyboard keyboardMarkup) {
        if (isBlank(message)) return;

        sendMessage.setChatId(chatId.toString());
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            crazyCookTelegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            //todo add logging to the project.
            e.printStackTrace();
        }
    }

    public void requestContact(Long chatId, String message, KeyboardButton keyboardButton) {
        KeyboardRow keyboardRaw = new KeyboardRow();
        keyboardRaw.add(keyboardButton);

        replyKeyboardMarkup.setKeyboard(List.of(keyboardRaw));
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        sendMessage(chatId, message, replyKeyboardMarkup);
    }

    public void editMessage(Integer messageId, Long chatId, String message) {
        editMessage(messageId, chatId, message, null);
    }

    public void hidePreviousButtons(Update update, Long chatId) {
        if (update.getCallbackQuery().getMessage().getReplyMarkup() != null) {
            editMessage(update.getCallbackQuery().getMessage().getMessageId(), chatId, update.getCallbackQuery().getMessage().getText(), Collections.emptyList());
        }
    }

    public void editMessage(Integer messageId, Long chatId, String message, List<List<InlineKeyboardButton>> buttons) {
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId.toString());
        editMessageText.setText(message);
        editMessageText.enableHtml(true);
        if (buttons != null) {
            keyboardMarkup.setKeyboard(buttons);
            editMessageText.setReplyMarkup(keyboardMarkup);
        }
        try {
            crazyCookTelegramBot.execute(editMessageText);
        } catch (TelegramApiException e) {
            //todo add logging to the project.
            e.printStackTrace();
        }
    }
}
