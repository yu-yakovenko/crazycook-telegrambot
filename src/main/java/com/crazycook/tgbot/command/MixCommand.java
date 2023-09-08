package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.addMoreButton;
import static com.crazycook.tgbot.bot.Buttons.chooseDeliveryButton;
import static com.crazycook.tgbot.bot.Buttons.mixFlavorForAllButton;
import static com.crazycook.tgbot.bot.Buttons.nextBoxButton;
import static com.crazycook.tgbot.bot.Buttons.showCartButton;

@AllArgsConstructor
public class MixCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;
    private final BoxService boxService;

    public static final String CART_COMPLETE = "Корзина сформована повністю.";
    public static final String BOX_COMPLETE = "Бокс заповнено міксом смаків.";

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);

        Cart cart = cartService.findCart(chatId, username);
        Box box = cart.getBoxInProgress();
        box.setIsMix(true);
        boxService.save(box);
        boolean moreBoxesPossible = cartService.isMoreBoxesPossible(cart);

        String message = BOX_COMPLETE;
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        if (moreBoxesPossible) {
            //Показати кнопу "перейти до заповнення нового боксу", "Для всіх інших боксів зробіть мікс смаків" та "показати що в корзині"
            buttons = List.of(List.of(nextBoxButton()), List.of(mixFlavorForAllButton()), List.of(showCartButton()));
        } else {
            //Показати повідомлення про те, що корзина повністю заповнена
            message += CART_COMPLETE;
            //Показати кнопки "показати що в корзині", "додати ще бокси" та "вибрати спосіб доставки"
            buttons = List.of(List.of(showCartButton()), List.of(addMoreButton()), List.of(chooseDeliveryButton()));
        }

        sendBotMessageService.sendMessage(chatId, message, buttons);
    }
}
