package com.crazycook.tgbot.command.cart;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.PromoService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.addMoreButton;
import static com.crazycook.tgbot.bot.Buttons.chooseDeliveryButton;
import static com.crazycook.tgbot.bot.Buttons.chooseFlavorsLongButton;
import static com.crazycook.tgbot.bot.Buttons.promoCodeButton;
import static com.crazycook.tgbot.bot.Buttons.refreshCartButton;
import static com.crazycook.tgbot.bot.Messages.ADDRESS;
import static com.crazycook.tgbot.bot.Messages.BOLD_END;
import static com.crazycook.tgbot.bot.Messages.BOLD_START;
import static com.crazycook.tgbot.bot.Messages.COMMENT;
import static com.crazycook.tgbot.bot.Messages.IN_YOUR_CART;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.OVERALL_PRICE;
import static com.crazycook.tgbot.bot.Messages.PRICE_WITH_PROMO;

@AllArgsConstructor
public class ShowCartCommand implements CrazyCookTGCommand {

    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;
    private final BoxService boxService;
    private final PromoService promoService;

    @Override
    @Transactional
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        Cart cart = cartService.findCart(chatId, username);

        StringBuilder message = new StringBuilder(IN_YOUR_CART);
        message.append(cartService.cartBoxesToString(cart));

        boolean emptyBoxes = cartService.containsEmptyBoxes(cart);
        boolean emptyCart = cart.getSNumber() + cart.getMNumber() + cart.getLNumber() == 0;

        if (cart.getComment() != null && !cart.getComment().isBlank()) {
            message.append(LINE_END).append(BOLD_START).append(COMMENT).append(BOLD_END).append(cart.getComment());
        }

        BigDecimal overallPrice = cartService.countOverallPrice(cart);
        boolean hasPromo = cart.getPromoCode() != null;
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        if (emptyBoxes) {
            buttons.add(List.of(addMoreButton(), refreshCartButton()));
            buttons.add(List.of(chooseFlavorsLongButton()));
            message.append(LINE_END).append(String.format(OVERALL_PRICE, overallPrice));
        } else if (!emptyCart) {
            buttons.add(List.of(addMoreButton(), chooseDeliveryButton()));
            buttons.add(List.of(promoCodeButton(), refreshCartButton()));
            message.append(LINE_END).append(String.format(OVERALL_PRICE, overallPrice));
        } else {
            buttons.add(List.of(addMoreButton()));
        }

        if (hasPromo && overallPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal promoPrice = promoService.countPromoPrice(overallPrice, cart.getPromoCode());
            message.append(LINE_END).append(String.format(PRICE_WITH_PROMO, cart.getPromoCode().getName(), promoPrice));
        }

        if (cart.getAddress() != null && !cart.getAddress().isBlank()) {
            message.append(LINE_END).append(ADDRESS).append(cart.getAddress());
        }

        sendBotMessageService.sendMessage(getChatId(update), message.toString(), buttons);
    }
}
