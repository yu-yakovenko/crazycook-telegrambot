package com.crazycook.tgbot.command;

import com.crazycook.tgbot.command.admin.AddNewFlavorCommand;
import com.crazycook.tgbot.command.admin.AddNewPromoCommand;
import com.crazycook.tgbot.command.admin.ChangeInStokCommand;
import com.crazycook.tgbot.command.admin.ChangeInStokMenuCommand;
import com.crazycook.tgbot.command.admin.ChangePriceCommand;
import com.crazycook.tgbot.command.admin.ChangePriceManuCommand;
import com.crazycook.tgbot.command.admin.CustomerMenuCommand;
import com.crazycook.tgbot.command.admin.MarkOrderAsDoneCommand;
import com.crazycook.tgbot.command.admin.ShowActiveOrdersCommand;
import com.crazycook.tgbot.command.admin.WaitingForFlavorCommand;
import com.crazycook.tgbot.command.admin.WaitingForNewPromoCommand;
import com.crazycook.tgbot.command.admin.WatingForPriceIdCommand;
import com.crazycook.tgbot.command.box.AddBoxCommand;
import com.crazycook.tgbot.command.box.ChooseBoxCommand;
import com.crazycook.tgbot.command.cart.CompleteCartCommand;
import com.crazycook.tgbot.command.cart.RefreshCartCommand;
import com.crazycook.tgbot.command.cart.ShowCartCommand;
import com.crazycook.tgbot.command.comment.CommentCommand;
import com.crazycook.tgbot.command.comment.CommentWaitingCommand;
import com.crazycook.tgbot.command.delivery.AddressCommand;
import com.crazycook.tgbot.command.delivery.ChooseDeliveryCommand;
import com.crazycook.tgbot.command.delivery.CourierCommand;
import com.crazycook.tgbot.command.delivery.DeliveryCommand;
import com.crazycook.tgbot.command.delivery.SelfPickupCommand;
import com.crazycook.tgbot.command.flavor.ChooseFlavorsCommand;
import com.crazycook.tgbot.command.flavor.FlavorIdCommand;
import com.crazycook.tgbot.command.flavor.FlavorInStockCommand;
import com.crazycook.tgbot.command.flavor.MixCommand;
import com.crazycook.tgbot.command.flavor.MixForRestCommand;
import com.crazycook.tgbot.command.promo.PromoCodeCommand;
import com.crazycook.tgbot.command.promo.PromoCodeWaitingCommand;
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
import com.google.common.collect.ImmutableMap;

import static com.crazycook.tgbot.command.CommandName.ADDRESS;
import static com.crazycook.tgbot.command.CommandName.ADD_BOX;
import static com.crazycook.tgbot.command.CommandName.ADD_MORE_BOXES;
import static com.crazycook.tgbot.command.CommandName.ADD_NEW_FLAVOR;
import static com.crazycook.tgbot.command.CommandName.ADD_NEW_PROMO;
import static com.crazycook.tgbot.command.CommandName.CHANGE_FLAVORS;
import static com.crazycook.tgbot.command.CommandName.CHANGE_FLAVOR_ID;
import static com.crazycook.tgbot.command.CommandName.CHANGE_PRICE;
import static com.crazycook.tgbot.command.CommandName.CHANGE_PRICE_MENU;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_BOX;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_DELIVERY;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_FLAVORS;
import static com.crazycook.tgbot.command.CommandName.COMMENT;
import static com.crazycook.tgbot.command.CommandName.COMPLETE_CART;
import static com.crazycook.tgbot.command.CommandName.CONTACT_COMMAND;
import static com.crazycook.tgbot.command.CommandName.COURIER;
import static com.crazycook.tgbot.command.CommandName.CUSTOMER_MENU;
import static com.crazycook.tgbot.command.CommandName.DELIVERY;
import static com.crazycook.tgbot.command.CommandName.FLAVOR;
import static com.crazycook.tgbot.command.CommandName.FLAVOR_ID;
import static com.crazycook.tgbot.command.CommandName.MARK_ORDER_AS_DONE;
import static com.crazycook.tgbot.command.CommandName.MIX_FLAVOR;
import static com.crazycook.tgbot.command.CommandName.MIX_FLAVOR_FOR_REST;
import static com.crazycook.tgbot.command.CommandName.PRICE;
import static com.crazycook.tgbot.command.CommandName.PROMO_CODE;
import static com.crazycook.tgbot.command.CommandName.REFRESH;
import static com.crazycook.tgbot.command.CommandName.SELF_PICKUP;
import static com.crazycook.tgbot.command.CommandName.SHOW_ACTIVE_ORDERS;
import static com.crazycook.tgbot.command.CommandName.SHOW_CART;
import static com.crazycook.tgbot.command.CommandName.START;
import static com.crazycook.tgbot.command.CommandName.UNKNOWN_COMMAND;
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_COMMENT;
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_NEW_FLAVOR;
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_NEW_PROMO;
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_PRICE;
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_PROMO_CODE;

public class CommandContainer {
    private final ImmutableMap<String, CrazyCookTGCommand> commandMap;
    private final CrazyCookTGCommand unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService, CartService cartService,
                            CustomerService customerService, FlavorService flavorService, BoxService boxService,
                            FlavorQuantityService flavorQuantityService, AdminService adminService,
                            OrderService orderService, PriceService priceService, PromoService promoService) {

        commandMap = ImmutableMap.<String, CrazyCookTGCommand>builder()
                .put(START.getCallbackData(), new StartCommand(sendBotMessageService, customerService, adminService))
                .put(PRICE.getCallbackData(), new PriceCommand(sendBotMessageService, priceService))
                .put(FLAVOR.getCallbackData(), new FlavorInStockCommand(sendBotMessageService, flavorService))
                .put(DELIVERY.getCallbackData(), new DeliveryCommand(sendBotMessageService, priceService))
                .put(CHOOSE_DELIVERY.getCallbackData(), new ChooseDeliveryCommand(sendBotMessageService, priceService))
                .put(COURIER.getCallbackData(), new CourierCommand(sendBotMessageService, cartService))
                .put(SELF_PICKUP.getCallbackData(), new SelfPickupCommand(sendBotMessageService, cartService))
                .put(CHOOSE_BOX.getCallbackData(), new ChooseBoxCommand(sendBotMessageService))
                .put(ADD_MORE_BOXES.getCallbackData(), new ChooseBoxCommand(sendBotMessageService))
                .put(ADD_BOX.getCallbackData(), new AddBoxCommand(sendBotMessageService, cartService, boxService))
                .put(SHOW_CART.getCallbackData(), new ShowCartCommand(sendBotMessageService, cartService))
                .put(REFRESH.getCallbackData(), new RefreshCartCommand(sendBotMessageService, cartService))
                .put(CHOOSE_FLAVORS.getCallbackData(), new ChooseFlavorsCommand(sendBotMessageService, flavorService, cartService))
                .put(MIX_FLAVOR.getCallbackData(), new MixCommand(sendBotMessageService, cartService, boxService))
                .put(MIX_FLAVOR_FOR_REST.getCallbackData(), new MixForRestCommand(sendBotMessageService, cartService, boxService))
                .put(FLAVOR_ID.getCallbackData(), new FlavorIdCommand(sendBotMessageService, flavorService, cartService, boxService, flavorQuantityService))
                .put(CONTACT_COMMAND.getCallbackData(), new ContactCommand(sendBotMessageService, customerService, cartService))
                .put(WAITING_FOR_COMMENT.getCallbackData(), new CommentWaitingCommand(sendBotMessageService, cartService))
                .put(ADDRESS.getCallbackData(), new AddressCommand(sendBotMessageService, cartService))
                .put(COMMENT.getCallbackData(), new CommentCommand(sendBotMessageService, cartService))
                .put(WAITING_FOR_PROMO_CODE.getCallbackData(), new PromoCodeWaitingCommand(sendBotMessageService, cartService))
                .put(PROMO_CODE.getCallbackData(), new PromoCodeCommand(sendBotMessageService, cartService, promoService))
                .put(COMPLETE_CART.getCallbackData(), new CompleteCartCommand(sendBotMessageService, customerService, cartService, adminService, orderService))
                .put(SHOW_ACTIVE_ORDERS.getCallbackData(), new ShowActiveOrdersCommand(sendBotMessageService, orderService, boxService, adminService, promoService))
                .put(MARK_ORDER_AS_DONE.getCallbackData(), new MarkOrderAsDoneCommand(sendBotMessageService, orderService, adminService))
                .put(ADD_NEW_FLAVOR.getCallbackData(), new AddNewFlavorCommand(sendBotMessageService, flavorService, adminService))
                .put(WAITING_FOR_NEW_FLAVOR.getCallbackData(), new WaitingForFlavorCommand(sendBotMessageService, adminService))
                .put(ADD_NEW_PROMO.getCallbackData(), new AddNewPromoCommand(sendBotMessageService, promoService, adminService))
                .put(WAITING_FOR_NEW_PROMO.getCallbackData(), new WaitingForNewPromoCommand(sendBotMessageService, adminService))
                .put(CUSTOMER_MENU.getCallbackData(), new CustomerMenuCommand(sendBotMessageService, adminService))
                .put(CHANGE_FLAVORS.getCallbackData(), new ChangeInStokMenuCommand(sendBotMessageService, flavorService, adminService))
                .put(CHANGE_FLAVOR_ID.getCallbackData(), new ChangeInStokCommand(sendBotMessageService, flavorService, adminService))
                .put(CHANGE_PRICE_MENU.getCallbackData(), new ChangePriceManuCommand(sendBotMessageService, priceService, adminService))
                .put(WAITING_FOR_PRICE.getCallbackData(), new WatingForPriceIdCommand(sendBotMessageService, adminService))
                .put(CHANGE_PRICE.getCallbackData(), new ChangePriceCommand(sendBotMessageService, priceService, adminService))
                .put(UNKNOWN_COMMAND.getCallbackData(), new UnknownCommand(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public CrazyCookTGCommand findCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}
