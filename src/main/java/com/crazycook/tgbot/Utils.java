package com.crazycook.tgbot;

import org.telegram.telegrambots.meta.api.objects.Update;

public class Utils {
    public static Long getChatId(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getChatId();
        } else if (update.hasCallbackQuery() && !update.getCallbackQuery().getData().isEmpty()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        //todo throw an exception
        return null;
    }

    public static String getMessage(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText().trim();
        } else if (update.hasCallbackQuery() && !update.getCallbackQuery().getData().isEmpty()) {
            return update.getCallbackQuery().getData();
        }
        return "";
    }

    public static String getUserName(Update update) {
        if (update.hasMessage() && update.getMessage().getFrom() != null) {
            return update.getMessage().getFrom().getUserName();
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getFrom() != null) {
            return update.getCallbackQuery().getFrom().getUserName();
        }
        return "";
    }
}
