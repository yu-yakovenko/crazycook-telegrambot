package com.crazycook.tgbot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;


public interface SendBotMessageService {


    void sendMessage(Long chatId, String message);

    void sendMessage(Long chatId, List<String> message);

    void sendMessage(Long chatId, String message, List<List<InlineKeyboardButton>> buttons);
}
