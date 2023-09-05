package com.crazycook.tgbot.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Buttons {

    public static final String CALLBACK_DATA_S = "/s";
    public static final String CALLBACK_DATA_M = "/m";
    public static final String CALLBACK_DATA_L = "/l";
    public static final String CALLBACK_DATA_PRICE = "/Price";
    public static final String CALLBACK_DATA_MENU = "/flavor";
    public static final String CALLBACK_DATA_ORDER = "/order";
    public static final String CALLBACK_DATA_MESSAGE = "/message";
    public static final String CALLBACK_DATA_ADD_MORE_BOXES = "/add_more_boxes";
    public static final String CALLBACK_DATA_CHOOSE_FLAVORS = "/choose_flavors";
    public static final String CALLBACK_DATA_SHOW_CART = "/show_cart";
    public static final String CALLBACK_DATA_START = "/start";
    public static final String CALLBACK_DATA_CHOOSE_BOX = "/choose_box";
    public static final String CALLBACK_DATA_REFRESH = "/refresh";
    public static final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();


    public static InlineKeyboardButton sButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("S 8 штук");
        yesButton.setCallbackData(CALLBACK_DATA_S);
        return yesButton;
    }

    public static InlineKeyboardButton mButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("M 12 штук");
        yesButton.setCallbackData(CALLBACK_DATA_M);
        return yesButton;
    }

    public static InlineKeyboardButton lButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("L 18 штук");
        yesButton.setCallbackData(CALLBACK_DATA_L);
        return yesButton;
    }

    public static InlineKeyboardButton startButton() {
        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Головне меню");
        startButton.setCallbackData(CALLBACK_DATA_START);
        return startButton;
    }

    public static InlineKeyboardButton priceButton() {
        InlineKeyboardButton priceButton = new InlineKeyboardButton();
        priceButton.setText("Бокси і ціни");
        priceButton.setCallbackData(CALLBACK_DATA_PRICE);
        return priceButton;
    }

    public static InlineKeyboardButton menuButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Смаки в наявності");
        yesButton.setCallbackData(CALLBACK_DATA_MENU);
        return yesButton;
    }

    public static InlineKeyboardButton createOrderButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Замовити бокс");
        yesButton.setCallbackData(CALLBACK_DATA_CHOOSE_BOX);
        return yesButton;
    }

    public static InlineKeyboardButton messageButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Написати нам повідомлення");
        yesButton.setCallbackData(CALLBACK_DATA_MESSAGE);
        return yesButton;
    }

    public static InlineKeyboardButton addMoreButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Додати бокси іншого розміру, або змінити кількість");
        yesButton.setCallbackData(CALLBACK_DATA_ADD_MORE_BOXES);
        return yesButton;
    }

    public static InlineKeyboardButton chooseFlavorsButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Це всі бокси, переходимо до вибору смаків");
        yesButton.setCallbackData(CALLBACK_DATA_CHOOSE_FLAVORS);
        return yesButton;
    }

    public static InlineKeyboardButton showCartButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Поркажи, що зараз знаходиться в моїй корзині");
        yesButton.setCallbackData(CALLBACK_DATA_SHOW_CART);
        return yesButton;
    }

    public static List<List<InlineKeyboardButton>> mainMenuButtons() {
        List<InlineKeyboardButton> buttonRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow2 = new ArrayList<>();
        buttonRow2.add(priceButton());
        buttonRow1.add(menuButton());
        buttonRow1.add(createOrderButton());
        buttonRow2.add(messageButton());
        return List.of(buttonRow1, buttonRow2);
    }

    public static List<List<InlineKeyboardButton>> cartInProgressButtons() {
        List<InlineKeyboardButton> buttonRow1 = List.of(addMoreButton());
        List<InlineKeyboardButton> buttonRow2 = List.of(chooseFlavorsButton());
        List<InlineKeyboardButton> buttonRow3 = List.of(showCartButton());
        return List.of(buttonRow1, buttonRow2, buttonRow3);
    }

}
