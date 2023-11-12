package com.crazycook.tgbot.bot;

import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Flavor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.crazycook.tgbot.command.CommandName.ADD_BOX;
import static com.crazycook.tgbot.command.CommandName.ADD_MORE_BOXES;
import static com.crazycook.tgbot.command.CommandName.CHANGE_FLAVORS;
import static com.crazycook.tgbot.command.CommandName.CHANGE_FLAVOR_ID;
import static com.crazycook.tgbot.command.CommandName.CHANGE_PRICE_MENU;
import static com.crazycook.tgbot.command.CommandName.CHANGE_PROMO_MENU;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_BOX;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_DELIVERY;
import static com.crazycook.tgbot.command.CommandName.CHOOSE_FLAVORS;
import static com.crazycook.tgbot.command.CommandName.COMPLETE_CART;
import static com.crazycook.tgbot.command.CommandName.COURIER;
import static com.crazycook.tgbot.command.CommandName.CUSTOMER_MENU;
import static com.crazycook.tgbot.command.CommandName.DELIVERY;
import static com.crazycook.tgbot.command.CommandName.FLAVOR;
import static com.crazycook.tgbot.command.CommandName.FLAVOR_ID;
import static com.crazycook.tgbot.command.CommandName.HUMAN_SUPPORT;
import static com.crazycook.tgbot.command.CommandName.MARK_ORDER_AS_DONE;
import static com.crazycook.tgbot.command.CommandName.MIX_FLAVOR;
import static com.crazycook.tgbot.command.CommandName.MIX_FLAVOR_FOR_REST;
import static com.crazycook.tgbot.command.CommandName.PRICE;
import static com.crazycook.tgbot.command.CommandName.REFRESH;
import static com.crazycook.tgbot.command.CommandName.SELF_PICKUP;
import static com.crazycook.tgbot.command.CommandName.SHOW_ACTIVE_ORDERS;
import static com.crazycook.tgbot.command.CommandName.SHOW_CART;
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_COMMENT;
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_NEW_FLAVOR;
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_NEW_PROMO;
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_PRICE;
import static com.crazycook.tgbot.command.CommandName.WAITING_FOR_PROMO_CODE;

public class Buttons {

    public static final String HUMAN_SUPPORT_URL = "https://t.me/CrazyCookKyiv";

    public static final String MINUS_ONE = "-1";
    public static final String PlUS_ONE = "+1";

    private static InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);
        return inlineKeyboardButton;
    }

    public static InlineKeyboardButton plusBoxSizeButton(BoxSize size) {
        return createButton(size.name() + " (" + size.getCapacity() + " макарон) " + PlUS_ONE, ADD_BOX.getCallbackData() + " " + size.name() + " " + PlUS_ONE);
    }

    public static InlineKeyboardButton minusBoxSizeButton(BoxSize size) {
        return createButton(MINUS_ONE, ADD_BOX.getCallbackData() + " " + size.name() + " " + MINUS_ONE);
    }

    public static List<List<InlineKeyboardButton>> boxSizeButtons() {
        return Arrays.stream(BoxSize.values())
                .map(size -> List.of(plusBoxSizeButton(size), minusBoxSizeButton(size)))
                .collect(Collectors.toList());
    }

    public static InlineKeyboardButton priceButton() {
        return createButton("\uD83D\uDCB0 Бокси і ціни", PRICE.getCallbackData());
    }

    public static InlineKeyboardButton flavorsButton() {
        return createButton("\uD83D\uDE0B Смаки в наявності", FLAVOR.getCallbackData());
    }

    public static InlineKeyboardButton chooseBoxButton() {
        return createButton("\uD83D\uDECD Замовити бокс", CHOOSE_BOX.getCallbackData());
    }

    public static InlineKeyboardButton humanSupportButton() {
        InlineKeyboardButton button = createButton("\uD83E\uDDD1\u200D\uD83D\uDCBB Написати повідомлення менеджеру", HUMAN_SUPPORT.getCallbackData());
        button.setUrl(HUMAN_SUPPORT_URL);
        return button;
    }

    public static InlineKeyboardButton deliveryButton() {
        return createButton("\uD83D\uDE80 Способи доставки", DELIVERY.getCallbackData());
    }

    public static InlineKeyboardButton addMoreButton() {
        return createButton("\uD83C\uDF81 Додати бокси", ADD_MORE_BOXES.getCallbackData());
    }

    public static InlineKeyboardButton chooseFlavorsButton() {
        return createButton("\uD83C\uDF53\uD83C\uDF70\uD83C\uDF78 Додати смаки", CHOOSE_FLAVORS.getCallbackData());
    }

    public static InlineKeyboardButton chooseFlavorsLongButton() {
        return createButton("\uD83C\uDF53\uD83C\uDF70\uD83C\uDF78 Додати смаки до пустих боксів", CHOOSE_FLAVORS.getCallbackData());
    }

    public static InlineKeyboardButton showCartButton() {
        return createButton("\uD83D\uDED2 Відобразити кошик", SHOW_CART.getCallbackData());
    }

    public static InlineKeyboardButton flavorIdButton(String name, Long id, String callBack) {
        return createButton(name, callBack + " " + id);
    }

    public static InlineKeyboardButton minusFlavorIdButton(Long id) {
        return createButton(MINUS_ONE, FLAVOR_ID.getCallbackData() + " " + id + " " + MINUS_ONE);
    }

    public static InlineKeyboardButton plusFlavorIdButton(Long id, String name) {
        return createButton(name + " " + PlUS_ONE, FLAVOR_ID.getCallbackData() + " " + id + " " + PlUS_ONE);
    }

    public static InlineKeyboardButton mixFlavorButton() {
        return createButton("Зберіть мені мікс.", MIX_FLAVOR.getCallbackData());
    }

    public static InlineKeyboardButton mixFlavorForAllButton() {
        return createButton("Для всіх пустих боксів зробіть мікс смаків", MIX_FLAVOR_FOR_REST.getCallbackData());
    }

    public static InlineKeyboardButton nextBoxButton() {
        return createButton("Перейти до заповнення наступного боксу", CHOOSE_FLAVORS.getCallbackData());
    }

    public static InlineKeyboardButton chooseDeliveryButton() {
        return createButton("\uD83D\uDE80 Вибрати спосіб доставки", CHOOSE_DELIVERY.getCallbackData());
    }

    public static InlineKeyboardButton selfPickupButton() {
        return createButton("Самовивіз", SELF_PICKUP.getCallbackData());
    }

    public static InlineKeyboardButton courierButton() {
        return createButton("Доставка кур'єром", COURIER.getCallbackData());
    }

    public static KeyboardButton requestContactButton() {
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Поділитися контактом");
        keyboardButton.setRequestContact(true);
        return keyboardButton;
    }

    public static InlineKeyboardButton commentButton() {
        return createButton("\uD83D\uDDD2 Додати коментар до замовлення", WAITING_FOR_COMMENT.getCallbackData());
    }

    public static InlineKeyboardButton completeCartButton() {
        return createButton("✅ Підтвердити замовлення", COMPLETE_CART.getCallbackData());
    }

    public static InlineKeyboardButton refreshCartButton() {
        return createButton("\uD83D\uDDD1 Очистити кошик", REFRESH.getCallbackData());
    }

    public static InlineKeyboardButton promoCodeButton() {
        return createButton("\uD83E\uDD11 Застосувати промокод", WAITING_FOR_PROMO_CODE.getCallbackData());
    }

    public static List<List<InlineKeyboardButton>> adminMainMenuButtons() {
        List<InlineKeyboardButton> buttonRow1 = List.of(showActiveOrdersButton(), changePriceButton());
        List<InlineKeyboardButton> buttonRow2 = List.of(addFlavorButton(), changeFlavorButton());
        List<InlineKeyboardButton> buttonRow3 = List.of(addPromoButton(), changePromoButton());
        List<InlineKeyboardButton> buttonRow4 = List.of(userMenuButton());

        return List.of(buttonRow1, buttonRow2, buttonRow3, buttonRow4);
    }

    public static InlineKeyboardButton showActiveOrdersButton() {
        return createButton("Активні замовлення", SHOW_ACTIVE_ORDERS.getCallbackData());
    }

    public static InlineKeyboardButton changePriceButton() {
        return createButton("Редагувати ціни", CHANGE_PRICE_MENU.getCallbackData());
    }

    public static InlineKeyboardButton changePriceIdButton(String message, Long id) {
        return createButton(message, WAITING_FOR_PRICE.getCallbackData() + " " + id);
    }

    public static InlineKeyboardButton changeFlavorButton() {
        return createButton("Редагувати cмаки", CHANGE_FLAVORS.getCallbackData());
    }

    public static InlineKeyboardButton addFlavorButton() {
        return createButton("Додати cмак", WAITING_FOR_NEW_FLAVOR.getCallbackData());
    }

    public static InlineKeyboardButton changePromoButton() {
        return createButton("Редагувати промокоди", CHANGE_PROMO_MENU.getCallbackData());
    }

    public static InlineKeyboardButton addPromoButton() {
        return createButton("Додати промокод", WAITING_FOR_NEW_PROMO.getCallbackData());
    }

    public static InlineKeyboardButton userMenuButton() {
        return createButton("Перейти в меню користувачів", CUSTOMER_MENU.getCallbackData());
    }

    public static InlineKeyboardButton setOrderAsDoneButton(Long id) {
        return createButton("Відмітити як виконаний", MARK_ORDER_AS_DONE.getCallbackData() + " " + id);
    }

    public static List<List<InlineKeyboardButton>> customerMenuButtons() {
        List<InlineKeyboardButton> buttonRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow2 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow3 = new ArrayList<>();
        buttonRow1.add(flavorsButton());
        buttonRow1.add(chooseBoxButton());
        buttonRow2.add(priceButton());
        buttonRow2.add(deliveryButton());
        buttonRow3.add(humanSupportButton());

        return List.of(buttonRow1, buttonRow2, buttonRow3);
    }

    public static List<List<InlineKeyboardButton>> cartCompleteButtons(boolean readyForComplete) {
        if (readyForComplete) {
            return List.of(
                    List.of(chooseDeliveryButton(), promoCodeButton()),
                    List.of(commentButton(), addMoreButton()),
                    List.of(completeCartButton()));
        } else {
            return List.of(
                    List.of(chooseDeliveryButton(), promoCodeButton()),
                    List.of(commentButton()),
                    List.of(addMoreButton()));
        }
    }

    public static List<List<InlineKeyboardButton>> moreBoxesPossibleButtons() {
        return List.of(
                List.of(nextBoxButton()),
                List.of(mixFlavorForAllButton()),
                List.of(showCartButton(), refreshCartButton()));
    }

    public static List<List<InlineKeyboardButton>> generateChangeFlavorButtons(List<Flavor> flavors) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int rowsNumber = flavors.size() / 3 + 1;
        int flavorIndex = 0;
        for (int i = 0; i < rowsNumber; i++) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();

            for (int j = 0; j < 3 && flavorIndex < flavors.size(); j++, flavorIndex++) {
                Flavor flavor = flavors.get(flavorIndex);
                buttonRow.add(flavorIdButton(flavor.getName(), flavor.getId(), CHANGE_FLAVOR_ID.getCallbackData()));
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
