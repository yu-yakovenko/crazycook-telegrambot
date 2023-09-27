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
import static com.crazycook.tgbot.bot.Buttons.moreBoxesPossibleButtons;
import static com.crazycook.tgbot.bot.Buttons.refreshCartButton;
import static com.crazycook.tgbot.bot.Buttons.showCartButton;
import static com.crazycook.tgbot.bot.Messages.BOX_COMPLETE;
import static com.crazycook.tgbot.bot.Messages.BOX_COMPLETE_MIX;
import static com.crazycook.tgbot.bot.Messages.CART_COMPLETE;
import static com.crazycook.tgbot.bot.Messages.FOUR_SPACES;
import static com.crazycook.tgbot.bot.Messages.LINE_END;

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
        int boxIndex = cartService.findCurrentBoxIndex(cart, box.getBoxSize());

        String message = String.format(BOX_COMPLETE, boxIndex, box.getBoxSize().name()) + LINE_END + FOUR_SPACES + BOX_COMPLETE_MIX;
        List<List<InlineKeyboardButton>> buttons;
        if (moreBoxesPossible) {
            //Показати кнопу "перейти до заповнення нового боксу", "Для всіх інших боксів зробіть мікс смаків"
            buttons = moreBoxesPossibleButtons();
        } else {
            //Показати повідомлення про те, що корзина повністю заповнена
            message += CART_COMPLETE;
            buttons = List.of(List.of(chooseDeliveryButton()), List.of(addMoreButton()), List.of(showCartButton(), refreshCartButton()));
        }

        sendBotMessageService.editMessage(update.getCallbackQuery().getMessage().getMessageId(), chatId, message, buttons);
    }
}
