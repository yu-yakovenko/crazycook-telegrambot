package com.crazycook.tgbot;

import com.crazycook.tgbot.command.CommandContainer;
import com.crazycook.tgbot.command.UnknownCommand;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Order;
import com.crazycook.tgbot.entity.OrderStatus;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.OrderService;
import com.crazycook.tgbot.service.SendBotMessageService;
import com.crazycook.tgbot.service.SendBotMessageServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.crazycook.tgbot.Buttons.CALLBACK_DATA_L;
import static com.crazycook.tgbot.Buttons.CALLBACK_DATA_M;
import static com.crazycook.tgbot.Buttons.CALLBACK_DATA_MENU;
import static com.crazycook.tgbot.Buttons.CALLBACK_DATA_ORDER;
import static com.crazycook.tgbot.Buttons.CALLBACK_DATA_PRICE;
import static com.crazycook.tgbot.Buttons.CALLBACK_DATA_S;
import static com.crazycook.tgbot.Buttons.CALLBACK_DATA_START;
import static com.crazycook.tgbot.Buttons.respondToMenuButton;
import static com.crazycook.tgbot.Buttons.respondToOrderButton;
import static com.crazycook.tgbot.Buttons.respondToPriceButton;
import static com.crazycook.tgbot.Buttons.respondToSMLButton;
import static com.crazycook.tgbot.Buttons.respondToStartButton;
import static com.crazycook.tgbot.command.CommandName.UNKNOWN_COMMAND;

@Component
public class CrazyCookTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";

    public final AppProperty property;

    private final OrderService orderService;
    private final CustomerService customerService;
    private final SendBotMessageService sendBotMessageService;
    private final CommandContainer commandContainer;

    public CrazyCookTelegramBot(AppProperty property, OrderService orderService, CustomerService customerService) {
        this.property = property;
        this.orderService = orderService;
        this.customerService = customerService;
        sendBotMessageService = new SendBotMessageServiceImpl(this);
        commandContainer = new CommandContainer(sendBotMessageService);
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
