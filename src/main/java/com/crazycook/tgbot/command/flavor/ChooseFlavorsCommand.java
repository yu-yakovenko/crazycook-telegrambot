package com.crazycook.tgbot.command.flavor;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.minusFlavorIdButton;
import static com.crazycook.tgbot.bot.Buttons.mixFlavorButton;
import static com.crazycook.tgbot.bot.Buttons.plusFlavorIdButton;
import static com.crazycook.tgbot.bot.Messages.IN_PROGRESS_BOX_MESSAGE;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.START_BOX_MESSAGE;

@AllArgsConstructor
public class ChooseFlavorsCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final FlavorService flavorService;
    private final CartService cartService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);

        List<Flavor> flavors = flavorService.getAllInStock().stream().toList();

        List<List<InlineKeyboardButton>> flavorButtons = generateFlavorButtons(flavors);
        flavorButtons.add(List.of(mixFlavorButton()));

        String message = handleBox(chatId, username);

        sendBotMessageService.hidePreviousButtons(update, chatId);
        sendBotMessageService.sendMessage(chatId, message, flavorButtons);
    }

    private String handleBox(Long chatId, String username) {
        Cart cart = cartService.findCart(chatId, username);
        String message;
        if (cart.getBoxInProgress() == null) {
            message = LINE_END + handleEmptyBox(cart);
        } else {
            message = LINE_END + handleNotEmptyBox(cart);
        }
        return message;
    }

    private String handleNotEmptyBox(Cart cart) {
        Box boxInProgress = cart.getBoxInProgress();
        BoxSize boxSize = boxInProgress.getBoxSize();

        int boxIndex = cartService.findCurrentBoxIndex(cart, boxSize);
        return String.format(IN_PROGRESS_BOX_MESSAGE, boxSize.name(), boxIndex);
    }

    private String handleEmptyBox(Cart cart) {
        BoxSize nextBoxSize = findNextSize(cart);
        cartService.createNewBoxInProgress(cart, nextBoxSize);

        int boxIndex = cartService.findCurrentBoxIndex(cart, nextBoxSize);
        return String.format(START_BOX_MESSAGE, nextBoxSize.name(), boxIndex);
    }

    private BoxSize findNextSize(Cart cart) {
        Set<Box> boxes = cartService.getBoxesForCart(cart.getId());
        long sInCart = boxes.stream().filter(b -> BoxSize.S.equals(b.getBoxSize())).count();
        if (sInCart < cart.getSNumber()) {
            return BoxSize.S;
        }
        long mInCart = boxes.stream().filter(b -> BoxSize.M.equals(b.getBoxSize())).count();
        if (mInCart < cart.getMNumber()) {
            return BoxSize.M;
        }
        long lInCart = boxes.stream().filter(b -> BoxSize.L.equals(b.getBoxSize())).count();
        if (lInCart < cart.getLNumber()) {
            return BoxSize.L;
        }
        return null;
    }

    private List<List<InlineKeyboardButton>> generateFlavorButtons(List<Flavor> flavors) {
        return flavors.stream()
                .map(f -> List.of(plusFlavorIdButton(f.getId(), f.getName()), minusFlavorIdButton(f.getId())))
                .collect(Collectors.toList());
    }
}
