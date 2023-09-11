package com.crazycook.tgbot.command.flavor;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.addMoreButton;
import static com.crazycook.tgbot.bot.Buttons.chooseDeliveryButton;
import static com.crazycook.tgbot.bot.Buttons.mixFlavorForAllButton;
import static com.crazycook.tgbot.bot.Buttons.nextBoxButton;
import static com.crazycook.tgbot.bot.Buttons.promoCodeButton;
import static com.crazycook.tgbot.bot.Buttons.showCartButton;
import static com.crazycook.tgbot.bot.Messages.BOX_COMPLETE_MIX;
import static com.crazycook.tgbot.bot.Messages.CART_COMPLETE;

@AllArgsConstructor
public class MixCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;
    private final BoxService boxService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);

        Cart cart = cartService.findCart(chatId, username);
        Box box = cart.getBoxInProgress();
        box.setIsMix(true);
        boxService.save(box);
        cart.setBoxInProgress(null);
        cartService.save(cart);
        boolean moreBoxesPossible = cartService.isMoreBoxesPossible(cart);

        String message = BOX_COMPLETE_MIX;
        List<List<InlineKeyboardButton>> buttons;
        if (moreBoxesPossible) {
            //Показати кнопу "перейти до заповнення нового боксу", "Для всіх інших боксів зробіть мікс смаків" та "показати що в корзині"
            buttons = List.of(List.of(nextBoxButton()), List.of(mixFlavorForAllButton()), List.of(showCartButton()));
        } else {
            //Показати повідомлення про те, що корзина повністю заповнена
            message += CART_COMPLETE;
            //Показати кнопки "показати що в корзині", "додати ще бокси" та "вибрати спосіб доставки"
            buttons = List.of(List.of(addMoreButton(), chooseDeliveryButton()), List.of(showCartButton(), promoCodeButton()));
        }

        sendBotMessageService.sendMessage(chatId, message, buttons);
    }
}
