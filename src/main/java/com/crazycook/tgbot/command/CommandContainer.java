package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.SendBotMessageService;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import static com.crazycook.tgbot.command.CommandName.*;

public class CommandContainer {
    private final ImmutableMap<String, CrazyCookTGCommand> commandMap;
    private final CrazyCookTGCommand unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService) {

        commandMap = ImmutableMap.<String, CrazyCookTGCommand>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService))
                .put(PRICE.getCommandName(), new PriceCommand(sendBotMessageService))
                .put(FLAVOR.getCommandName(), new FlavorCommand(sendBotMessageService))
                .put(CHOOSE_BOX.getCommandName(), new ChooseBoxCommand(sendBotMessageService))
                .put(UNKNOWN_COMMAND.getCommandName(), new StartCommand(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public CrazyCookTGCommand findCommand(String commandIdentifier, String username) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}
