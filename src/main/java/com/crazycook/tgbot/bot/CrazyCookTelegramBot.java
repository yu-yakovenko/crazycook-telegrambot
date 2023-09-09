package com.crazycook.tgbot.bot;

import com.crazycook.tgbot.AppProperty;
import com.crazycook.tgbot.Utils;
import com.crazycook.tgbot.command.CommandContainer;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.FlavorQuantityService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.command.CommandName.BOX_NUMBER_COMMAND;
import static com.crazycook.tgbot.command.CommandName.COMMENT;
import static com.crazycook.tgbot.command.CommandName.CONTACT_COMMAND;
import static com.crazycook.tgbot.command.CommandName.FLAVOR_NUMBER_COMMAND;
import static com.crazycook.tgbot.command.CommandName.UNKNOWN_COMMAND;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_BOX_NUMBER_STATUSES;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_COMMENT;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_FLAVOR_NUMBER;

@Component
public class CrazyCookTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";
    public static String regExOnlyIntNumbers = "[0-9]+";
    public static String regExOnlyNumbers = "^[+-]?([0-9]+(([.]|[,])[0-9]*)?|([.]|[,])[0-9]+)$";

    public final AppProperty property;
    private final SendBotMessageService sendBotMessageService;
    private final CommandContainer commandContainer;
    private final CartService cartService;

    public CrazyCookTelegramBot(AppProperty property, CartService cartService, CustomerService customerService,
                                FlavorService flavorService, BoxService boxService,
                                FlavorQuantityService flavorQuantityService, AdminService adminService) {
        this.property = property;
        this.cartService = cartService;
        sendBotMessageService = new SendBotMessageService(this);
        commandContainer = new CommandContainer(sendBotMessageService, cartService, customerService, flavorService,
                boxService, flavorQuantityService, adminService);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);

        String message = Utils.getMessage(update);
        Cart cart = cartService.createOrFind(chatId, username);
        if (WAITING_FOR_COMMENT.equals(cart.getStatus())) {
            commandContainer.findCommand(COMMENT.getCommandName()).execute(update);
        } else if (update.getMessage() != null && update.getMessage().hasContact()) {
            commandContainer.findCommand(CONTACT_COMMAND.getCommandName()).execute(update);
        } else if (message.startsWith(COMMAND_PREFIX)) {
            String commandIdentifier = message.split(" ")[0].toLowerCase();
            commandContainer.findCommand(commandIdentifier.toLowerCase(Locale.ROOT)).execute(update);
        } else if (message.matches(regExOnlyNumbers) && WAITING_BOX_NUMBER_STATUSES.contains(cart.getStatus())) {
            commandContainer.findCommand(BOX_NUMBER_COMMAND.getCommandName()).execute(update);
        } else if (message.matches(regExOnlyIntNumbers) && WAITING_FOR_FLAVOR_NUMBER.equals(cart.getStatus())) {
            commandContainer.findCommand(FLAVOR_NUMBER_COMMAND.getCommandName()).execute(update);
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
