package com.crazycook.tgbot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class Bot {

    private final CrazyCookTelegramBot crazyCookTelegramBot;

    public Bot(CrazyCookTelegramBot crazyCookTelegramBot) {
        this.crazyCookTelegramBot = crazyCookTelegramBot;
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(crazyCookTelegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
