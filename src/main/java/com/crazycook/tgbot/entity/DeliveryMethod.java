package com.crazycook.tgbot.entity;

public enum DeliveryMethod {
    SELF_PICKUP("самовивіз"), COURIER("курьєром");

    private String name;

    DeliveryMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
