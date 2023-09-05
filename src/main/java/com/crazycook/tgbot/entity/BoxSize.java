package com.crazycook.tgbot.entity;

public enum BoxSize {
    S(8), M(12), L(18);
    private Integer flavorNumber;

    BoxSize(Integer flavorNumber) {
        this.flavorNumber = flavorNumber;
    }

    public Integer getFlavorNumber() {
        return flavorNumber;
    }
}