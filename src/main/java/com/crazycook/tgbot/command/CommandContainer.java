package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.FlavorQuantityService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import com.google.common.collect.ImmutableMap;

import static com.crazycook.tgbot.command.CommandName.ADD_MORE_BOXES;
import static com.crazycook.tgbot.command.CommandName.BOX_NUMBER_COMMAND;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_BOX;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_FLAVORS;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_L;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_M;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_S;
import static com.crazycook.tgbot.command.CommandName.FLAVOR;
import static com.crazycook.tgbot.command.CommandName.FLAVOR_ID;
import static com.crazycook.tgbot.command.CommandName.FLAVOR_NUMBER_COMMAND;
import static com.crazycook.tgbot.command.CommandName.MIX_FLAVOR;
import static com.crazycook.tgbot.command.CommandName.PRICE;
import static com.crazycook.tgbot.command.CommandName.SHOW_CART;
import static com.crazycook.tgbot.command.CommandName.START;
import static com.crazycook.tgbot.command.CommandName.UNKNOWN_COMMAND;

public class CommandContainer {
    private final ImmutableMap<String, CrazyCookTGCommand> commandMap;
    private final CrazyCookTGCommand unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService, CartService cartService,
                            CustomerService customerService, FlavorService flavorService, BoxService boxService,
                            FlavorQuantityService flavorQuantityService) {

        commandMap = ImmutableMap.<String, CrazyCookTGCommand>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, customerService))
                .put(PRICE.getCommandName(), new PriceCommand(sendBotMessageService))
                .put(FLAVOR.getCommandName(), new FlavorCommand(sendBotMessageService, flavorService))
                .put(CHOOSE_BOX.getCommandName(), new ChooseBoxCommand(sendBotMessageService))
                .put(ADD_MORE_BOXES.getCommandName(), new ChooseBoxCommand(sendBotMessageService))
                .put(CHOOSE_S.getCommandName(), new ChooseSCommand(sendBotMessageService, cartService))
                .put(CHOOSE_M.getCommandName(), new ChooseMCommand(sendBotMessageService, cartService))
                .put(CHOOSE_L.getCommandName(), new ChooseLCommand(sendBotMessageService, cartService))
                .put(BOX_NUMBER_COMMAND.getCommandName(), new BoxNumberCommand(sendBotMessageService, cartService))
                .put(FLAVOR_NUMBER_COMMAND.getCommandName(), new FlavorNumberCommand(sendBotMessageService, flavorService, cartService, boxService, flavorQuantityService))
                .put(SHOW_CART.getCommandName(), new ShowCartCommand(sendBotMessageService, cartService, boxService))
                .put(CHOOSE_FLAVORS.getCommandName(), new ChooseFlavorsCommand(sendBotMessageService, flavorService, cartService, boxService))
                .put(MIX_FLAVOR.getCommandName(), new MixCommand(sendBotMessageService, cartService, boxService))
                .put(FLAVOR_ID.getCommandName(), new FlavorIdCommand(sendBotMessageService, flavorService, cartService))
                .put(UNKNOWN_COMMAND.getCommandName(), new UnknownCommand(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public CrazyCookTGCommand findCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}
