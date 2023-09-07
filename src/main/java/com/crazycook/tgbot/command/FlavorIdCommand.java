package com.crazycook.tgbot.command;

import com.crazycook.tgbot.Utils;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.entity.CartStatus.WAITING_FOR_FLAVOR_NUMBER;

@AllArgsConstructor
public class FlavorIdCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final FlavorService flavorService;
    private final CartService cartService;

    public final static String FLAVOR_NUMBER_MESSAGE = "Напиши цифрою скільки макаронів зі смаком ʼ%sʼ ти хочеш додати до цього боксу?\n";

    @Override
    @Transactional
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        String message = Utils.getMessage(update);
        Cart cart = cartService.createOrFind(chatId, username);
        String flavorId = message.split(" ")[1].toLowerCase();

        Flavor flavor = flavorService.getById(flavorId);
        String flavorName = flavor.getName();

        cart.setCurrentFlavor(flavor);
        cart.setStatus(WAITING_FOR_FLAVOR_NUMBER);
        cartService.save(cart);

        sendBotMessageService.sendMessage(chatId, String.format(FLAVOR_NUMBER_MESSAGE, flavorName));
    }
}
