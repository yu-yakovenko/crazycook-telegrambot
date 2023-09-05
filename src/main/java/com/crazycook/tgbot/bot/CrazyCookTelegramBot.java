package com.crazycook.tgbot.bot;

import com.crazycook.tgbot.AppProperty;
import com.crazycook.tgbot.Utils;
import com.crazycook.tgbot.command.CommandContainer;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.SendBotMessageService;
import com.crazycook.tgbot.service.SendBotMessageServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

import static com.crazycook.tgbot.command.CommandName.UNKNOWN_COMMAND;

@Component
public class CrazyCookTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";

    public final AppProperty property;
    private final SendBotMessageService sendBotMessageService;
    private final CommandContainer commandContainer;

    public CrazyCookTelegramBot(AppProperty property, CartService cartService, CustomerService customerService) {
        this.property = property;
        sendBotMessageService = new SendBotMessageServiceImpl(this);
        commandContainer = new CommandContainer(sendBotMessageService, cartService, customerService);
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = Utils.getMessage(update);
        String username = Utils.getUserName(update);
        if (message.startsWith(COMMAND_PREFIX)) {
            String commandIdentifier = message.split(" ")[0].toLowerCase();
            commandContainer.findCommand(commandIdentifier.toLowerCase(Locale.ROOT), username).execute(update);
        } else {
            commandContainer.findCommand(UNKNOWN_COMMAND.name(), username).execute(update);
        }

    }

    @Override
    public String getBotToken() {
        return property.getToken();
    }

    @Override
    public String getBotUsername() {
        return property.getUsername();
    }
}
