package com.crazycook.tgbot.entity;

import java.util.List;

public enum CartStatus {

    IN_PROGRESS,
    WAITING_FOR_S_NUMBER,
    WAITING_FOR_M_NUMBER,
    WAITING_FOR_L_NUMBER,
    WAITING_FOR_FLAVOR_NUMBER,
    WAITING_FOR_ADDRESS,
    WAITING_FOR_COMMENT;

    public static final List<CartStatus> WAITING_BOX_NUMBER_STATUSES = List.of(WAITING_FOR_S_NUMBER, WAITING_FOR_M_NUMBER, WAITING_FOR_L_NUMBER);
}
