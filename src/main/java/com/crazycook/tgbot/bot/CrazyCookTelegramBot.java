package com.crazycook.tgbot.bot;

import com.crazycook.tgbot.AppProperty;
import com.crazycook.tgbot.Utils;
import com.crazycook.tgbot.command.CommandContainer;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.command.CommandName.BOX_NUMBER_COMMAND;
import static com.crazycook.tgbot.command.CommandName.UNKNOWN_COMMAND;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_STATUSES;

@Component
public class CrazyCookTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";
    public static String regExOnlyNumbers = "[0-9]+";

    public final AppProperty property;
    private final SendBotMessageService sendBotMessageService;
    private final CommandContainer commandContainer;
    private final CartService cartService;

    public CrazyCookTelegramBot(AppProperty property, CartService cartService, CustomerService customerService,
                                FlavorService flavorService, BoxService boxService) {
        this.property = property;
        this.cartService = cartService;
        sendBotMessageService = new SendBotMessageService(this);
        commandContainer = new CommandContainer(sendBotMessageService, cartService, customerService, flavorService,
                boxService);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
        String message = Utils.getMessage(update);
        Cart cart = cartService.createOrFind(chatId);
        if (message.startsWith(COMMAND_PREFIX)) {
            String commandIdentifier = message.split(" ")[0].toLowerCase();
            commandContainer.findCommand(commandIdentifier.toLowerCase(Locale.ROOT)).execute(update);
        } else if (message.matches(regExOnlyNumbers) && WAITING_STATUSES.contains(cart.getStatus())) {
            commandContainer.findCommand(BOX_NUMBER_COMMAND.getCommandName()).execute(update);
        } else {
            commandContainer.findCommand(UNKNOWN_COMMAND.getCommandName()).execute(update);
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
