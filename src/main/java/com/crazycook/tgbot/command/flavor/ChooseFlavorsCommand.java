package com.crazycook.tgbot.command.flavor;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.entity.FlavorQuantity;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Set;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.addButton;
import static com.crazycook.tgbot.bot.Buttons.generateFlavorButtons;
import static com.crazycook.tgbot.bot.Buttons.mixFlavorButton;

@AllArgsConstructor
public class ChooseFlavorsCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final FlavorService flavorService;
    private final CartService cartService;
    private final BoxService boxService;

    public final static String START_BOX_MESSAGE = "Починаємо збирати %s бокс номер %d. Додай смак до боксу:";
    public final static String IN_PROGRESS_BOX_MESSAGE = "Продовжуємо збирати %s бокс номер %d. Зараз в ньому вже є %d макарон. Можна додати ще %d. Додай смак до боксу:";

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);

        List<Flavor> flavors = flavorService.getAllInStock().stream().toList();

        List<List<InlineKeyboardButton>> flavorButtons = generateFlavorButtons(flavors);
        addMixFlavorButton(flavorButtons);

        String message = handleBox(chatId, username);
        sendBotMessageService.sendMessage(chatId, message, flavorButtons);
    }

    private void addMixFlavorButton(List<List<InlineKeyboardButton>> flavorButtons) {
        addButton(flavorButtons, 3, mixFlavorButton());
    }

    private String handleBox(Long chatId, String username) {
        Cart cart = cartService.findCart(chatId, username);
        String message;
        if (cart.getBoxInProgress() == null) {
            message = handleEmptyBox(cart);
        } else {
            message = handleNotEmptyBox(cart);
        }
        return message;
    }

    private String handleNotEmptyBox(Cart cart) {
        Box boxInProgress = cart.getBoxInProgress();
        BoxSize boxSize = boxInProgress.getBoxSize();
        List<FlavorQuantity> flavorQuantities = boxService.getFlavorQuantitiesForBox(boxInProgress.getId());

        int occupiedNumber = flavorQuantities.stream().map(FlavorQuantity::getQuantity).reduce(Integer::sum).orElse(0);
        int vacantNumber = boxSize.getCapacity() - occupiedNumber;

        int boxIndex = cartService.findCurrentBoxIndex(cart, boxSize);
        return String.format(IN_PROGRESS_BOX_MESSAGE, boxSize.name(), boxIndex, occupiedNumber, vacantNumber);
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
}
