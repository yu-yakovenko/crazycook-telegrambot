package com.crazycook.tgbot;

import com.crazycook.tgbot.entity.Order;
import com.crazycook.tgbot.entity.OrderStatus;
import com.crazycook.tgbot.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    public static final String CALLBACK_DATA_MENU = "/Menu";
    public static final String CALLBACK_DATA_ORDER = "/Order";
    public static final String CALLBACK_DATA_MESSAGE = "/Message";
    public static final String CALLBACK_DATA_START = "/Start";
    public static final String CALLBACK_DATA_REFRESH = "/Refresh";
    public static final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();


    public static InlineKeyboardButton sButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("S");
        yesButton.setCallbackData(CALLBACK_DATA_S);
        return yesButton;
    }

    public static InlineKeyboardButton mButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("M");
        yesButton.setCallbackData(CALLBACK_DATA_S);
        return yesButton;
    }

    public static InlineKeyboardButton lButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("L");
        yesButton.setCallbackData(CALLBACK_DATA_S);
        return yesButton;
    }

    public static InlineKeyboardButton startButton() {
        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Головне меню");
        startButton.setCallbackData(CALLBACK_DATA_START);
        return startButton;
    }

    public static void respondToStartButton(SendMessage sendMessage, List<List<InlineKeyboardButton>> buttons) {
        sendMessage.setText("Обери що тебе цікавить:");
        List<InlineKeyboardButton> buttonRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow2 = new ArrayList<>();
        buttonRow2.add(priceButton());
        buttonRow1.add(menuButton());
        buttonRow1.add(orderButton());
        buttonRow2.add(messageButton());
        buttons.add(buttonRow1);
        buttons.add(buttonRow2);

        keyboardMarkup.setKeyboard(buttons);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    public static InlineKeyboardButton priceButton() {
        InlineKeyboardButton priceButton = new InlineKeyboardButton();
        priceButton.setText("Бокси і ціни");
        priceButton.setCallbackData(CALLBACK_DATA_PRICE);
        return priceButton;
    }

    public static void respondToPriceButton(SendMessage sendMessage, List<List<InlineKeyboardButton>> buttons) {

    }

    public static InlineKeyboardButton menuButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Смаки в наявності");
        yesButton.setCallbackData(CALLBACK_DATA_MENU);
        return yesButton;
    }

    public static void respondToMenuButton(SendMessage sendMessage, List<List<InlineKeyboardButton>> buttons) {
        sendMessage.setText("Зараз в наявності є такі смаки: \n" +
                "- манго-маракуйя; \n" +
                "- шоколад-банан; \n" +
                "- космополітан; \n" +
                "- пролуничне мохіто; \n");
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(startButton());
        buttonRow.add(orderButton());
        buttons.add(buttonRow);

        keyboardMarkup.setKeyboard(buttons);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    public static InlineKeyboardButton orderButton() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Замовити бокс");
        yesButton.setCallbackData(CALLBACK_DATA_ORDER);
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

    public static void defaultRespond(SendMessage sendMessage, List<List<InlineKeyboardButton>> buttons) {
        sendMessage.setText("Вибач, я не розумію, твоє повідомлення. Ось головне меню, обери що тебе цікавить:");
        keyboardMarkup.setKeyboard(mainMenuButtons());
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    public static List<List<InlineKeyboardButton>> mainMenuButtons() {
        List<InlineKeyboardButton> buttonRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow2 = new ArrayList<>();
        buttonRow2.add(priceButton());
        buttonRow1.add(menuButton());
        buttonRow1.add(orderButton());
        buttonRow2.add(messageButton());
        return List.of(buttonRow1, buttonRow2);
    }


}
