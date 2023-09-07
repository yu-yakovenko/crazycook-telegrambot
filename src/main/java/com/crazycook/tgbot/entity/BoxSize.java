package com.crazycook.tgbot.entity;

public enum BoxSize {
    S(8), M(12), L(18);
    private Integer capacity;

    BoxSize(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCapacity() {
        return capacity;
    }
}