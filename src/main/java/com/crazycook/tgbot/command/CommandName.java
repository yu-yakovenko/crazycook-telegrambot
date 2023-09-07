package com.crazycook.tgbot.command;

public enum CommandName {

    CHOOSE_S("/s"),
    CHOOSE_M("/m"),
    CHOOSE_L("/l"),
    PRICE("/price"),
    CHOOSE_BOX("/choose_box"),
    FLAVOR("/flavor"),
    FLAVOR_ID("/flavor_id"),
    MIX_FLAVOR("/mix_flavor"),
    ORDER("/order"),
    MESSAGE("/message"),
    START("/start"),
    UNKNOWN_COMMAND("/unknown_command"),
    BOX_NUMBER_COMMAND("BoxNumberCommand"),
    ADD_MORE_BOXES("/add_more_boxes"),
    CHOOSE_FLAVORS("/choose_flavors"),
    SHOW_CART("/show_cart"),
    REFRESH("/refresh");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
