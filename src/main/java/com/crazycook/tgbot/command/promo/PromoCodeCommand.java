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

import java.util.List;
import java.util.Optional;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.cartCompleteButtons;
import static com.crazycook.tgbot.bot.Messages.PROMO_ADDED;
import static com.crazycook.tgbot.bot.Messages.PROMO_EXPIRED;
import static com.crazycook.tgbot.bot.Messages.WRONG_PROMO;
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
        if (optPromo.isPresent()) {
            Promo promo = optPromo.get();
            if (promoService.isNotExpired(promo)) {
                cart.setPromoCode(optPromo.get());
                message = String.format(PROMO_ADDED, promo.getName(), promo.getPercent());
            } else {
                message = String.format(PROMO_EXPIRED, promo.getExpiringDate());
            }
        } else {
            message = WRONG_PROMO;
        }
        cartService.save(cart);

        List<List<InlineKeyboardButton>> buttons = cartCompleteButtons(cartService.readyForComplete(cart));

        sendBotMessageService.sendMessage(customerChatId, message, buttons);
    }
}
