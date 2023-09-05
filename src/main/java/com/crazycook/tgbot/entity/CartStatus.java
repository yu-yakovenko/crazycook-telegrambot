package com.crazycook.tgbot.entity;

import java.util.List;

public enum CartStatus {
    NEW, WAITING_FOR_S_NUMBER, WAITING_FOR_M_NUMBER, WAITING_FOR_L_NUMBER, WAITING_FOR_APPROVE, ASSEMBLED, DONE;

    public static final List<CartStatus> WAITING_STATUSES = List.of(WAITING_FOR_S_NUMBER, WAITING_FOR_M_NUMBER, WAITING_FOR_L_NUMBER);
}
