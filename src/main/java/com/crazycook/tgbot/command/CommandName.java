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
    ADDRESS("WaitingForAddressCommand"),
    COMMENT("CommentCommand"),
    WAITING_FOR_COMMENT("/waiting_for_comment"),
    COMPLETE_CART("/complete_cart"),
    CONTACT_COMMAND("ContactCommand"),
    WAITING_FOR_PROMO_CODE("/waiting_for_promo_code"),
    PROMO_CODE("PromoCodeWaitingCommand"),

    SHOW_ACTIVE_ORDERS("/active_orders"),
    MARK_ORDER_AS_DONE("/order_done"),
    WAITING_FOR_NEW_FLAVOR("/add_flavor"),
    ADD_NEW_FLAVOR("AddNewFlavorCommand"),
    WAITING_FOR_NEW_PROMO("/add_promo"),
    ADD_NEW_PROMO("AddNewPromoCommand"),
    CUSTOMER_MENU("/user_menu"),
    CHANGE_FLAVORS("/change_flavors"),
    CHANGE_FLAVOR_ID("/change_flavor_id"),
    UNKNOWN_COMMAND("/unknown_command");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
