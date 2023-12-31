package com.crazycook.tgbot.command;

public enum CommandName {

    PRICE("/price"),
    ORDER("/order"),
    HUMAN_SUPPORT("/message"),
    START("/start"),
    SHOW_CART("/show_cart"),
    REFRESH("/refresh"),

    FLAVOR("/flavor"),
    FLAVOR_ID("/flavor_id"),
    MIX_FLAVOR("/mix_flavor"),
    MIX_FLAVOR_FOR_REST("/mix_flavor_for_rest"),
    CHOOSE_FLAVORS("/choose_flavors"),

    CHOOSE_BOX("/choose_box"),
    BOX_NUMBER_COMMAND("BoxNumberCommand"),
    ADD_MORE_BOXES("/add_more_boxes"),
    ADD_BOX("/box"),

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
    CHANGE_PRICE_MENU("/change_price"),
    CHANGE_PROMO_MENU("/change_promo"),
    WAITING_FOR_PRICE("/change_price_id"),
    CHANGE_PRICE("ChangePriceCommand"),
    UNKNOWN_COMMAND("/unknown_command");


    private final String callbackData;

    CommandName(String commandName) {
        this.callbackData = commandName;
    }

    public String getCallbackData() {
        return callbackData;
    }
}
