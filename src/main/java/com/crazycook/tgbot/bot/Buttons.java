package com.crazycook.tgbot.bot;

import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Flavor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Buttons {

    public static final String CALLBACK_DATA_BOX_SIZE = "/box";
    public static final String CALLBACK_DATA_PRICE = "/Price";
    public static final String CALLBACK_DATA_FLAVOR = "/flavor";
    public static final String CALLBACK_DATA_FLAVOR_ID = "/flavor_id";
    public static final String CALLBACK_DATA_CHANGE_FLAVOR_ID = "/change_flavor_id";
    public static final String CALLBACK_DATA_MIX_FLAVOR = "/mix_flavor";
    public static final String CALLBACK_DATA_MIX_FLAVOR_FOR_REST = "/mix_flavor_for_rest";
    public static final String CALLBACK_DATA_MESSAGE = "/message";
    public static final String CALLBACK_DATA_ADD_MORE_BOXES = "/add_more_boxes";
    public static final String CALLBACK_DATA_CHOOSE_FLAVORS = "/choose_flavors";
    public static final String CALLBACK_DATA_SHOW_CART = "/show_cart";
    public static final String CALLBACK_DATA_CHOOSE_BOX = "/choose_box";
    public static final String CALLBACK_DATA_DELIVERY = "/delivery";
    public static final String CALLBACK_DATA_CHOOSE_DELIVERY = "/choose_delivery";
    public static final String CALLBACK_DATA_SELF_PICKUP = "/self_pickup";
    public static final String CALLBACK_DATA_COURIER = "/courier";
    public static final String CALLBACK_DATA_WAITING_FOR_COMMENT = "/waiting_for_comment";
    public static final String CALLBACK_DATA_COMPLETE_CART = "/complete_cart";
    public static final String CALLBACK_DATA_REFRESH = "/refresh";
    public static final String CALLBACK_DATA_PROMO_CODE = "/waiting_for_promo_code";
    public static final String CALLBACK_DATA_ACTIVE_ORDERS = "/active_orders";
    public static final String CALLBACK_DATA_CHANGE_PRICE = "/change_price";
    public static final String CALLBACK_DATA_CHANGE_PRICE_ID = "/change_price_id";
    public static final String CALLBACK_DATA_CHANGE_FLAVORS = "/change_flavors";
    public static final String CALLBACK_DATA_CHANGE_PROMO = "/change_promo";
    public static final String CALLBACK_DATA_ADD_FLAVOR = "/add_flavor";
    public static final String CALLBACK_DATA_ADD_PROMO = "/add_promo";
    public static final String CALLBACK_DATA_USER_MENU = "/user_menu";
    public static final String CALLBACK_DATA_MARK_ORDER_AS_DONE = "/order_done";
    public static final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();


    private static InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);
        return inlineKeyboardButton;
    }

    public static InlineKeyboardButton boxSizeButton(BoxSize size) {
        return createButton(size.name() + " " + size.getCapacity() + " штук", CALLBACK_DATA_BOX_SIZE + " " + size.name());
    }

    public static List<InlineKeyboardButton> boxSizeButtons() {
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        Arrays.stream(BoxSize.values()).forEach(size -> buttonRow.add(boxSizeButton(size)));
        return buttonRow;
    }

    public static InlineKeyboardButton priceButton() {
        return createButton("\uD83D\uDCB0 Бокси і ціни", CALLBACK_DATA_PRICE);
    }

    public static InlineKeyboardButton flavorsButton() {
        return createButton("\uD83D\uDE0B Смаки в наявності", CALLBACK_DATA_FLAVOR);
    }

    public static InlineKeyboardButton chooseBoxButton() {
        return createButton("\uD83D\uDECD Замовити бокс", CALLBACK_DATA_CHOOSE_BOX);
    }

    public static InlineKeyboardButton messageButton() {
        return createButton("Написати повідомлення менеджеру", CALLBACK_DATA_MESSAGE);
    }

    public static InlineKeyboardButton deliveryButton() {
        return createButton("\uD83D\uDE80 Способи доставки", CALLBACK_DATA_DELIVERY);
    }

    public static InlineKeyboardButton addMoreButton() {
        return createButton("\uD83C\uDF81 Додати бокси", CALLBACK_DATA_ADD_MORE_BOXES);
    }

    public static InlineKeyboardButton chooseFlavorsButton() {
        return createButton("\uD83C\uDF53\uD83C\uDF70\uD83C\uDF78 Додати смаки", CALLBACK_DATA_CHOOSE_FLAVORS);
    }

    public static InlineKeyboardButton chooseFlavorsLongButton() {
        return createButton("\uD83C\uDF53\uD83C\uDF70\uD83C\uDF78 Додати смаки до пустих боксів", CALLBACK_DATA_CHOOSE_FLAVORS);
    }

    public static InlineKeyboardButton showCartButton() {
        return createButton("\uD83D\uDED2 Покажи, що знаходиться в корзині", CALLBACK_DATA_SHOW_CART);
    }

    public static InlineKeyboardButton flavorIdButton(String name, Long id, String callBack) {
        return createButton(name, callBack + " " + id);
    }

    public static InlineKeyboardButton mixFlavorButton() {
        return createButton("Зберіть мені мікс.", CALLBACK_DATA_MIX_FLAVOR);
    }

    public static InlineKeyboardButton mixFlavorForAllButton() {
        return createButton("Для всіх інших боксів зробіть мікс смаків", CALLBACK_DATA_MIX_FLAVOR_FOR_REST);
    }

    public static InlineKeyboardButton nextBoxButton() {
        return createButton("Перейти до заповнення наступного боксу", CALLBACK_DATA_CHOOSE_FLAVORS);
    }

    public static InlineKeyboardButton chooseDeliveryButton() {
        return createButton("\uD83D\uDE80 Вибрати спосіб доставки", CALLBACK_DATA_CHOOSE_DELIVERY);
    }

    public static InlineKeyboardButton selfPickupButton() {
        return createButton("Самовивіз", CALLBACK_DATA_SELF_PICKUP);
    }

    public static InlineKeyboardButton courierButton() {
        return createButton("Доставка кур'єром", CALLBACK_DATA_COURIER);
    }

    public static KeyboardButton requestContactButton() {
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Поділитися контактом");
        keyboardButton.setRequestContact(true);
        return keyboardButton;
    }

    public static InlineKeyboardButton commentButton() {
        return createButton("\uD83D\uDDD2 Залишити коментар", CALLBACK_DATA_WAITING_FOR_COMMENT);
    }

    public static InlineKeyboardButton completeCartButton() {
        return createButton("✅ Підтвердити замовлення", CALLBACK_DATA_COMPLETE_CART);
    }

    public static InlineKeyboardButton refreshCartButton() {
        return createButton("\uD83D\uDDD1 Очистити корзину", CALLBACK_DATA_REFRESH);
    }

    public static InlineKeyboardButton promoCodeButton() {
        return createButton("\uD83E\uDD11 Застосувати промокод", CALLBACK_DATA_PROMO_CODE);
    }

    public static List<List<InlineKeyboardButton>> adminMainMenuButtons() {
        List<InlineKeyboardButton> buttonRow1 = List.of(showActiveOrdersButton(), changePriceButton());
        List<InlineKeyboardButton> buttonRow2 = List.of(addFlavorButton(), changeFlavorButton());
        List<InlineKeyboardButton> buttonRow3 = List.of(addPromoButton(), changePromoButton());
        List<InlineKeyboardButton> buttonRow4 = List.of(userMenuButton());

        return List.of(buttonRow1, buttonRow2, buttonRow3, buttonRow4);
    }

    public static InlineKeyboardButton showActiveOrdersButton() {
        return createButton("Активні замовлення", CALLBACK_DATA_ACTIVE_ORDERS);
    }

    public static InlineKeyboardButton changePriceButton() {
        return createButton("Редагувати ціни", CALLBACK_DATA_CHANGE_PRICE);
    }

    public static InlineKeyboardButton changePriceIdButton(String message, Long id) {
        return createButton(message, CALLBACK_DATA_CHANGE_PRICE_ID + " " + id);
    }

    public static InlineKeyboardButton changeFlavorButton() {
        return createButton("Редагувати cмаки", CALLBACK_DATA_CHANGE_FLAVORS);
    }

    public static InlineKeyboardButton addFlavorButton() {
        return createButton("Додати cмак", CALLBACK_DATA_ADD_FLAVOR);
    }

    public static InlineKeyboardButton changePromoButton() {
        return createButton("Редагувати промокоди", CALLBACK_DATA_CHANGE_PROMO);
    }

    public static InlineKeyboardButton addPromoButton() {
        return createButton("Додати промокод", CALLBACK_DATA_ADD_PROMO);
    }

    public static InlineKeyboardButton userMenuButton() {
        return createButton("Перейти в меню користувачів", CALLBACK_DATA_USER_MENU);
    }

    public static InlineKeyboardButton setOrderAsDoneButton(Long id) {
        return createButton("Відмітити як виконаний", CALLBACK_DATA_MARK_ORDER_AS_DONE + " " + id);
    }

    public static List<List<InlineKeyboardButton>> customerMenuButtons() {
        List<InlineKeyboardButton> buttonRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow2 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow3 = new ArrayList<>();
        buttonRow2.add(priceButton());
        buttonRow1.add(flavorsButton());
        buttonRow1.add(chooseBoxButton());
        buttonRow2.add(deliveryButton());
//        todo return messageButton when implement functionality
//         buttonRow3.add(messageButton());

        return List.of(buttonRow1, buttonRow2, buttonRow3);
    }

    public static List<List<InlineKeyboardButton>> cartInProgressButtons() {
        List<InlineKeyboardButton> buttonRow1 = List.of(addMoreButton(), chooseFlavorsButton());
        List<InlineKeyboardButton> buttonRow3 = List.of(showCartButton());
        return List.of(buttonRow1, buttonRow3);
    }

    public static List<List<InlineKeyboardButton>> generateFlavorButtons(List<Flavor> flavors, String callBack) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int rowsNumber = flavors.size() / 3 + 1;
        int flavorIndex = 0;
        for (int i = 0; i < rowsNumber; i++) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();

            for (int j = 0; j < 3 && flavorIndex < flavors.size(); j++, flavorIndex++) {
                Flavor flavor = flavors.get(flavorIndex);
                buttonRow.add(flavorIdButton(flavor.getName(), flavor.getId(), callBack));
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
