package com.crazycook.tgbot.command.flavor;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Set;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.chooseDeliveryButton;
import static com.crazycook.tgbot.bot.Messages.BOXES_COMPLETE;
import static com.crazycook.tgbot.bot.Messages.CART_COMPLETE;

@AllArgsConstructor
public class MixForRestCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;
    private final BoxService boxService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);

        Cart cart = cartService.findCart(chatId, username);
        Set<Box> boxes = cartService.getBoxesForCart(cart.getId());
        createBoxesIfNeeded(cart.getSNumber(), BoxSize.S, cart, boxes);
        createBoxesIfNeeded(cart.getMNumber(), BoxSize.M, cart, boxes);
        createBoxesIfNeeded(cart.getLNumber(), BoxSize.L, cart, boxes);

        cart.setBoxInProgress(null);
        cart.setBoxes(boxes);
        cartService.save(cart);

        //Показати повідомлення про те, що корзина повністю заповнена
        String message = BOXES_COMPLETE + CART_COMPLETE;

        sendBotMessageService.hidePreviousButtons(update, chatId);
        sendBotMessageService.sendMessage(chatId, message, List.of(List.of(chooseDeliveryButton())));
    }

    private void createBoxesIfNeeded(int existsNumber, BoxSize boxSize, Cart cart, Set<Box> boxes) {
        if (existsNumber > 0) {
            long exists = boxes.stream().filter(b -> boxSize.equals(b.getBoxSize())).count();
            long dif = existsNumber - exists;
            for (int i = 0; i < dif; i++) {
                Box box = Box.builder().boxSize(boxSize).cart(cart).isMix(true).build();
                boxService.save(box);
                boxes.add(box);
            }
        }
    }
}
