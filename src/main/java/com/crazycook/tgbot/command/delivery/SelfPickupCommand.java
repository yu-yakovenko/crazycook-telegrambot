package com.crazycook.tgbot.command.delivery;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.requestContactButton;
import static com.crazycook.tgbot.entity.DeliveryMethod.SELF_PICKUP;

@AllArgsConstructor
public class SelfPickupCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;

    public static final String REQUEST_CONTACT = "Будь ласка поділіться своїм контактом, щоб ми могли звязатися з вами для уточнення деталей доставки";

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);

        Cart cart = cartService.createOrFind(chatId, username);
        cart.setDeliveryMethod(SELF_PICKUP);
        cartService.save(cart);

        sendBotMessageService.requestContact(chatId, REQUEST_CONTACT, requestContactButton());
    }
}
