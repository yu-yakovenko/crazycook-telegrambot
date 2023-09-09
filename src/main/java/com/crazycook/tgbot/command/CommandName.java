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
    MIX_FLAVOR_FOR_REST("/mix_flavor_for_rest"),
    CHOOSE_FLAVORS("/choose_flavors"),
    FLAVOR_NUMBER_COMMAND("FlavorNumberCommand"),

    CHOOSE_S("/s"),
    CHOOSE_M("/m"),
    CHOOSE_L("/l"),
    CHOOSE_BOX("/choose_box"),
    BOX_NUMBER_COMMAND("BoxNumberCommand"),
    ADD_MORE_BOXES("/add_more_boxes"),

    DELIVERY("/delivery"),
    CHOOSE_DELIVERY("/choose_delivery"),
    COURIER("/courier"),
    SELF_PICKUP("/self_pickup"),
    COMMENT("CommentCommand"),
    WAITING_FOR_COMMENT("/waiting_for_comment"),
    COMPLETE_CART("/complete_cart"),
    CONTACT_COMMAND("ContactCommand"),
    UNKNOWN_COMMAND("/unknown_command");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
