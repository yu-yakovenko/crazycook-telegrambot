package com.crazycook.tgbot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Buttons {

    public static final String CALLBACK_DATA_S = "/S";
    public static final String CALLBACK_DATA_M = "/M";
    public static final String CALLBACK_DATA_L = "/L";
    public static final String CALLBACK_DATA_PRICE = "/Price";
    public static final String CALLBACK_DATA_MENU = "/Flavor";
    public static final String CALLBACK_DATA_ORDER = "/Order";
    public static final String CALLBACK_DATA_MESSAGE = "/Message";
    public static final String CALLBACK_DATA_START = "/Start";
    public static final String CALLBACK_DATA_CHOOSE_BOX = "/choose_box";
    public static final String CALLBACK_DATA_REFRESH = "/Refresh";
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
        yesButton.setCallbackData(CALLBACK_DATA_S);
        return yesButton;
    }

    public static InlineKeyboardButton lButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("L 18 штук");
        yesButton.setCallbackData(CALLBACK_DATA_S);
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

    public static void respondToOrderButton(SendMessage sendMessage, List<List<InlineKeyboardButton>> buttons) {
        sendMessage.setText("Вибери розмір боксу: \n");
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(sButton());
        buttonRow.add(mButton());
        buttonRow.add(lButton());
        buttons.add(buttonRow);

        keyboardMarkup.setKeyboard(buttons);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    public static void respondToSMLButton(SendMessage sendMessage, List<List<InlineKeyboardButton>> buttons) {
        sendMessage.setText("Напиши цифрою скільки таких боксів ти хочеш? \n");
    }

    public static InlineKeyboardButton messageButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Написати нам повідомлення");
        yesButton.setCallbackData(CALLBACK_DATA_MESSAGE);
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


}
