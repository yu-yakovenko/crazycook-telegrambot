package com.crazycook.tgbot.bot;

import com.crazycook.tgbot.entity.Flavor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Buttons {

    public static final String CALLBACK_DATA_S = "/s";
    public static final String CALLBACK_DATA_M = "/m";
    public static final String CALLBACK_DATA_L = "/l";
    public static final String CALLBACK_DATA_PRICE = "/Price";
    public static final String CALLBACK_DATA_FLAVOR = "/flavor";
    public static final String CALLBACK_DATA_FLAVOR_ID = "/flavor_id";
    public static final String CALLBACK_DATA_MIX_FLAVOR = "/mix_flavor";
    public static final String CALLBACK_DATA_MIX_FLAVOR_FOR_ALL = "/mix_flavor_for_all";
    public static final String CALLBACK_DATA_ORDER = "/order";
    public static final String CALLBACK_DATA_MESSAGE = "/message";
    public static final String CALLBACK_DATA_ADD_MORE_BOXES = "/add_more_boxes";
    public static final String CALLBACK_DATA_CHOOSE_FLAVORS = "/choose_flavors";
    public static final String CALLBACK_DATA_SHOW_CART = "/show_cart";
    public static final String CALLBACK_DATA_COMPLETE_CART = "/complete_cart";
    public static final String CALLBACK_DATA_START = "/start";
    public static final String CALLBACK_DATA_CHOOSE_BOX = "/choose_box";
    public static final String CALLBACK_DATA_CHOOSE_DELIVERY = "/choose_delivery";
    public static final String CALLBACK_DATA_REFRESH = "/refresh";
    public static final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();


    private static InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);
        return inlineKeyboardButton;
    }

    public static InlineKeyboardButton sButton() {
        return createButton("S 8 штук", CALLBACK_DATA_S);
    }

    public static InlineKeyboardButton mButton() {
        return createButton("M 12 штук", CALLBACK_DATA_M);
    }

    public static InlineKeyboardButton lButton() {
        return createButton("L 18 штук", CALLBACK_DATA_L);
    }

    public static InlineKeyboardButton startButton() {
        return createButton("Головне меню", CALLBACK_DATA_START);
    }

    public static InlineKeyboardButton priceButton() {
        return createButton("Бокси і ціни", CALLBACK_DATA_PRICE);
    }

    public static InlineKeyboardButton flavorsButton() {
        return createButton("Смаки в наявності", CALLBACK_DATA_FLAVOR);
    }

    public static InlineKeyboardButton createOrderButton() {
        return createButton("Замовити бокс", CALLBACK_DATA_CHOOSE_BOX);
    }

    public static InlineKeyboardButton messageButton() {
        return createButton("Написати нам повідомлення", CALLBACK_DATA_MESSAGE);
    }

    public static InlineKeyboardButton addMoreButton() {
        return createButton("Додати бокси іншого розміру, або змінити кількість", CALLBACK_DATA_ADD_MORE_BOXES);
    }

    public static InlineKeyboardButton chooseFlavorsButton() {
        return createButton("Це всі бокси, переходимо до вибору смаків", CALLBACK_DATA_CHOOSE_FLAVORS);
    }

    public static InlineKeyboardButton showCartButton() {
        return createButton("Покажи, що зараз знаходиться в моїй корзині", CALLBACK_DATA_SHOW_CART);
    }

    public static InlineKeyboardButton flavorIdButton(String name, Long id) {
        return createButton(name, CALLBACK_DATA_FLAVOR_ID + " " + id);
    }

    public static InlineKeyboardButton mixFlavorButton() {
        return createButton("Зберіть мені мікс.", CALLBACK_DATA_MIX_FLAVOR);
    }

    public static InlineKeyboardButton mixFlavorForAllButton() {
        return createButton("Для всіх інших боксів зробіть мікс смаків", CALLBACK_DATA_MIX_FLAVOR_FOR_ALL);
    }

    public static InlineKeyboardButton nextBoxButton() {
        return createButton("Перейти до заповнення наступного боксу", CALLBACK_DATA_CHOOSE_FLAVORS);
    }

    public static InlineKeyboardButton chooseDeliveryButton() {
        return createButton("Перейти до вибору способу доставки", CALLBACK_DATA_CHOOSE_DELIVERY);
    }

    public static List<List<InlineKeyboardButton>> mainMenuButtons() {
        List<InlineKeyboardButton> buttonRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow2 = new ArrayList<>();
        buttonRow2.add(priceButton());
        buttonRow1.add(flavorsButton());
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

    public static List<List<InlineKeyboardButton>> generateFlavorButtons(List<Flavor> flavors) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int rowsNumber = flavors.size() % 3 + 1;
        int flavorIndex = 0;
        for (int i = 0; i < rowsNumber; i++) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();

            for (int j = 0; j < 3 && flavorIndex < flavors.size(); j++, flavorIndex++) {
                Flavor flavor = flavors.get(flavorIndex);
                buttonRow.add(flavorIdButton(flavor.getName(), flavor.getId()));
            }

            buttons.add(buttonRow);
        }
        return buttons;
    }

    public static void addButton(List<List<InlineKeyboardButton>> buttons, int maxButtonsInRow, InlineKeyboardButton newButton) {
        if (buttons.get(buttons.size() - 1).size() < maxButtonsInRow) {
            buttons.get(buttons.size() - 1).add(newButton);
        } else {
            buttons.add(List.of(newButton));
        }
    }
}
