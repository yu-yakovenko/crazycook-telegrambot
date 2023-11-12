package com.crazycook.tgbot.bot;

import com.crazycook.tgbot.AppProperty;
import com.crazycook.tgbot.Utils;
import com.crazycook.tgbot.command.CommandContainer;
import com.crazycook.tgbot.entity.AdminStatus;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.FlavorQuantityService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.OrderService;
import com.crazycook.tgbot.service.PriceService;
import com.crazycook.tgbot.service.PromoService;
import com.crazycook.tgbot.service.SendBotMessageService;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.command.CommandName.ADDRESS;
import static com.crazycook.tgbot.command.CommandName.ADD_NEW_FLAVOR;
import static com.crazycook.tgbot.command.CommandName.ADD_NEW_PROMO;
import static com.crazycook.tgbot.command.CommandName.BOX_NUMBER_COMMAND;
import static com.crazycook.tgbot.command.CommandName.CHANGE_PRICE;
import static com.crazycook.tgbot.command.CommandName.COMMENT;
import static com.crazycook.tgbot.command.CommandName.CONTACT_COMMAND;
import static com.crazycook.tgbot.command.CommandName.PROMO_CODE;
import static com.crazycook.tgbot.command.CommandName.UNKNOWN_COMMAND;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_BOX_NUMBER_STATUSES;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_ADDRESS;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_COMMENT;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_PROMO;

@Component
public class CrazyCookTelegramBot extends TelegramLongPollingBot {

    private static final Logger log = Logger.getLogger(CrazyCookTelegramBot.class);

    public static final String COMMAND_PREFIX = "/";
    public static final String regExOnlyIntNumbers = "[0-9]+";
    public static final String regExOnlyNumbers = "^[+-]?([0-9]+(([.]|[,])[0-9]*)?|([.]|[,])[0-9]+)$";

    public final AppProperty property;
    private final SendBotMessageService sendBotMessageService;
    private final CommandContainer commandContainer;
    private final CartService cartService;
    private final AdminService adminService;

    public CrazyCookTelegramBot(AppProperty property, CartService cartService, CustomerService customerService,
                                FlavorService flavorService, BoxService boxService,
                                FlavorQuantityService flavorQuantityService, AdminService adminService,
                                OrderService orderService, PriceService priceService, PromoService promoService) {
        this.property = property;
        this.cartService = cartService;
        this.adminService = adminService;
        sendBotMessageService = new SendBotMessageService(this);
        commandContainer = new CommandContainer(sendBotMessageService, cartService, customerService, flavorService,
                boxService, flavorQuantityService, adminService, orderService, priceService, promoService);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        boolean isAdmin = adminService.checkIsAdmin(chatId);

        String message = Utils.getMessage(update);
        Cart cart = cartService.createOrFind(chatId, username);
         if (update.getMessage() != null && update.getMessage().hasContact()) {
             commandContainer.findCommand(CONTACT_COMMAND.getCallbackData()).execute(update);
         } else if (message.startsWith(COMMAND_PREFIX)) {
             String commandIdentifier = message.split(" ")[0].toLowerCase();
             commandContainer.findCommand(commandIdentifier.toLowerCase(Locale.ROOT)).execute(update);
         } else if (WAITING_FOR_PROMO.equals(cart.getStatus())) {
             commandContainer.findCommand(PROMO_CODE.getCallbackData()).execute(update);
         } else if (WAITING_FOR_ADDRESS.equals(cart.getStatus())) {
             commandContainer.findCommand(ADDRESS.getCallbackData()).execute(update);
         } else if (WAITING_FOR_COMMENT.equals(cart.getStatus())) {
             commandContainer.findCommand(COMMENT.getCallbackData()).execute(update);
         } else if (message.matches(regExOnlyNumbers) && WAITING_BOX_NUMBER_STATUSES.contains(cart.getStatus())) {
             commandContainer.findCommand(BOX_NUMBER_COMMAND.getCallbackData()).execute(update);
         } else if (isAdmin && adminService.isHasStatus(chatId, AdminStatus.WAITING_FOR_FLAVOR)) {
             commandContainer.findCommand(ADD_NEW_FLAVOR.getCallbackData()).execute(update);
         } else if (isAdmin && adminService.isHasStatus(chatId, AdminStatus.WAITING_FOR_PROMO)) {
             commandContainer.findCommand(ADD_NEW_PROMO.getCallbackData()).execute(update);
         } else if (isAdmin && adminService.isHasStatus(chatId, AdminStatus.WAITING_FOR_PRICE)) {
             commandContainer.findCommand(CHANGE_PRICE.getCallbackData()).execute(update);
         } else {
             commandContainer.findCommand(UNKNOWN_COMMAND.getCallbackData()).execute(update);
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
