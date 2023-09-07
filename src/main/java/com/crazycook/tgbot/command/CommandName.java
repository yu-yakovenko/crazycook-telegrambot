package com.crazycook.tgbot.command;

public enum CommandName {

    PRICE("/price"),
    ORDER("/order"),
    MESSAGE("/message"),
    START("/start"),
    SHOW_CART("/show_cart"),
    REFRESH("/refresh"),

    FLAVOR("/flavor"),
    FLAVOR_ID("/flavor_id"),
    MIX_FLAVOR("/mix_flavor"),
    CHOOSE_FLAVORS("/choose_flavors"),
    FLAVOR_NUMBER_COMMAND("FlavorNumberCommand"),

    CHOOSE_S("/s"),
    CHOOSE_M("/m"),
    CHOOSE_L("/l"),
    CHOOSE_BOX("/choose_box"),
    BOX_NUMBER_COMMAND("BoxNumberCommand"),
    ADD_MORE_BOXES("/add_more_boxes"),

    UNKNOWN_COMMAND("/unknown_command");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
