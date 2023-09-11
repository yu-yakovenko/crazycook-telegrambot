package com.crazycook.tgbot.command;

import com.crazycook.tgbot.command.admin.AddNewFlavorCommand;
import com.crazycook.tgbot.command.admin.AddNewPromoCommand;
import com.crazycook.tgbot.command.admin.CustomerMenuCommand;
import com.crazycook.tgbot.command.admin.MarkOrderAsDoneCommand;
import com.crazycook.tgbot.command.admin.ShowActiveOrdersCommand;
import com.crazycook.tgbot.command.admin.WaitingForFlavorCommand;
import com.crazycook.tgbot.command.admin.WaitingForNewPromoCommand;
import com.crazycook.tgbot.command.box.BoxNumberCommand;
import com.crazycook.tgbot.command.box.ChooseBoxCommand;
import com.crazycook.tgbot.command.box.ChooseLCommand;
import com.crazycook.tgbot.command.box.ChooseMCommand;
import com.crazycook.tgbot.command.box.ChooseSCommand;
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
import com.crazycook.tgbot.command.flavor.FlavorNumberCommand;
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
import static com.crazycook.tgbot.command.CommandName.ADD_MORE_BOXES;
import static com.crazycook.tgbot.command.CommandName.ADD_NEW_FLAVOR;
import static com.crazycook.tgbot.command.CommandName.ADD_NEW_PROMO;
import static com.crazycook.tgbot.command.CommandName.BOX_NUMBER_COMMAND;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_BOX;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_DELIVERY;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_FLAVORS;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_L;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_M;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_S;
import static com.crazycook.tgbot.command.CommandName.COMMENT;
import static com.crazycook.tgbot.command.CommandName.COMPLETE_CART;
import static com.crazycook.tgbot.command.CommandName.CONTACT_COMMAND;
import static com.crazycook.tgbot.command.CommandName.COURIER;
import static com.crazycook.tgbot.command.CommandName.CUSTOMER_MENU;
import static com.crazycook.tgbot.command.CommandName.DELIVERY;
import static com.crazycook.tgbot.command.CommandName.FLAVOR;
import static com.crazycook.tgbot.command.CommandName.FLAVOR_ID;
import static com.crazycook.tgbot.command.CommandName.FLAVOR_NUMBER_COMMAND;
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
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_PROMO_CODE;

public class CommandContainer {
    private final ImmutableMap<String, CrazyCookTGCommand> commandMap;
    private final CrazyCookTGCommand unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService, CartService cartService,
                            CustomerService customerService, FlavorService flavorService, BoxService boxService,
                            FlavorQuantityService flavorQuantityService, AdminService adminService,
                            OrderService orderService, PriceService priceService, PromoService promoService) {

        commandMap = ImmutableMap.<String, CrazyCookTGCommand>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, customerService, adminService))
                .put(PRICE.getCommandName(), new PriceCommand(sendBotMessageService, priceService))
                .put(FLAVOR.getCommandName(), new FlavorInStockCommand(sendBotMessageService, flavorService))
                .put(DELIVERY.getCommandName(), new DeliveryCommand(sendBotMessageService, priceService))
                .put(CHOOSE_DELIVERY.getCommandName(), new ChooseDeliveryCommand(sendBotMessageService, priceService))
                .put(COURIER.getCommandName(), new CourierCommand(sendBotMessageService, cartService))
                .put(SELF_PICKUP.getCommandName(), new SelfPickupCommand(sendBotMessageService, cartService))
                .put(CHOOSE_BOX.getCommandName(), new ChooseBoxCommand(sendBotMessageService))
                .put(ADD_MORE_BOXES.getCommandName(), new ChooseBoxCommand(sendBotMessageService))
                .put(CHOOSE_S.getCommandName(), new ChooseSCommand(sendBotMessageService, cartService))
                .put(CHOOSE_M.getCommandName(), new ChooseMCommand(sendBotMessageService, cartService))
                .put(CHOOSE_L.getCommandName(), new ChooseLCommand(sendBotMessageService, cartService))
                .put(BOX_NUMBER_COMMAND.getCommandName(), new BoxNumberCommand(sendBotMessageService, cartService, adminService))
                .put(FLAVOR_NUMBER_COMMAND.getCommandName(), new FlavorNumberCommand(sendBotMessageService, flavorService, cartService, boxService, flavorQuantityService))
                .put(SHOW_CART.getCommandName(), new ShowCartCommand(sendBotMessageService, cartService, boxService, promoService))
                .put(REFRESH.getCommandName(), new RefreshCartCommand(sendBotMessageService, cartService))
                .put(CHOOSE_FLAVORS.getCommandName(), new ChooseFlavorsCommand(sendBotMessageService, flavorService, cartService, boxService))
                .put(MIX_FLAVOR.getCommandName(), new MixCommand(sendBotMessageService, cartService, boxService))
                .put(MIX_FLAVOR_FOR_REST.getCommandName(), new MixForRestCommand(sendBotMessageService, cartService, boxService))
                .put(FLAVOR_ID.getCommandName(), new FlavorIdCommand(sendBotMessageService, flavorService, cartService))
                .put(CONTACT_COMMAND.getCommandName(), new ContactCommand(sendBotMessageService, customerService))
                .put(WAITING_FOR_COMMENT.getCommandName(), new CommentWaitingCommand(sendBotMessageService, cartService))
                .put(ADDRESS.getCommandName(), new AddressCommand(sendBotMessageService, cartService))
                .put(COMMENT.getCommandName(), new CommentCommand(sendBotMessageService, cartService))
                .put(WAITING_FOR_PROMO_CODE.getCommandName(), new PromoCodeWaitingCommand(sendBotMessageService, cartService))
                .put(PROMO_CODE.getCommandName(), new PromoCodeCommand(sendBotMessageService, cartService, promoService))
                .put(COMPLETE_CART.getCommandName(), new CompleteCartCommand(sendBotMessageService, customerService, cartService, boxService, adminService, orderService, promoService))
                .put(SHOW_ACTIVE_ORDERS.getCommandName(), new ShowActiveOrdersCommand(sendBotMessageService, orderService, boxService, adminService, promoService))
                .put(MARK_ORDER_AS_DONE.getCommandName(), new MarkOrderAsDoneCommand(sendBotMessageService, orderService, adminService))
                .put(ADD_NEW_FLAVOR.getCommandName(), new AddNewFlavorCommand(sendBotMessageService, flavorService, adminService))
                .put(WAITING_FOR_NEW_FLAVOR.getCommandName(), new WaitingForFlavorCommand(sendBotMessageService, adminService))
                .put(ADD_NEW_PROMO.getCommandName(), new AddNewPromoCommand(sendBotMessageService, promoService, adminService))
                .put(WAITING_FOR_NEW_PROMO.getCommandName(), new WaitingForNewPromoCommand(sendBotMessageService, adminService))
                .put(CUSTOMER_MENU.getCommandName(), new CustomerMenuCommand(sendBotMessageService, adminService))
                .put(UNKNOWN_COMMAND.getCommandName(), new UnknownCommand(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public CrazyCookTGCommand findCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}
