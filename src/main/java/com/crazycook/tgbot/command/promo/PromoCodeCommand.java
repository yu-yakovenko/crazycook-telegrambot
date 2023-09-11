package com.crazycook.tgbot.command.promo;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Promo;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.PromoService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.commentButton;
import static com.crazycook.tgbot.bot.Buttons.completeCartButton;
import static com.crazycook.tgbot.bot.Buttons.promoCodeButton;
import static com.crazycook.tgbot.bot.Buttons.showCartButton;
import static com.crazycook.tgbot.entity.CartStatus.IN_PROGRESS;

@AllArgsConstructor
public class PromoCodeCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;
    private final PromoService promoService;

    @Override
    public void execute(Update update) {
        Long customerChatId = getChatId(update);
        String customerUsername = getUserName(update);
        String promoCodeName = getMessage(update);

        String message;
        Cart cart = cartService.findCart(customerChatId, customerUsername);
        cart.setStatus(IN_PROGRESS);
        Optional<Promo> optPromo = promoService.findByName(promoCodeName);
        boolean isPromoSuccess = false;
        if (optPromo.isPresent()) {
            Promo promo = optPromo.get();
            if (promoService.isNotExpired(promo)) {
                cart.setPromoCode(optPromo.get());
                message = "Додали промокод " + promo.getName() + ", що дає знижку " + promo.getPercent() + "%.";
                isPromoSuccess = true;
            } else {
                message = "Вибач, цей промокод вже просрочено, він діяв до " + promo.getExpiringDate() + "";
            }
        } else {
            message = "Такого промокоду в нас немає.";
        }
        cartService.save(cart);

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(completeCartButton(), commentButton()));
        if (isPromoSuccess) {
            buttons.add(List.of(showCartButton()));
        } else {
            buttons.add(List.of(promoCodeButton(), showCartButton()));
        }

        sendBotMessageService.sendMessage(customerChatId, message, buttons);
    }
}
