package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.flavorIdButton;
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

        List<Flavor> flavors = flavorService.getAllInStock().stream().toList();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int rowsNumber = flavors.size() % 3 + 1;
        int flavorIndex = 0;
        for (int i = 0; i < rowsNumber; i++) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            for (int j = 0; j < 3 && flavorIndex < flavors.size(); j++, flavorIndex++) {
                buttonRow.add(flavorIdButton(flavors.get(flavorIndex).getName(), flavors.get(flavorIndex).getId()));
            }
            buttons.add(buttonRow);
        }

        if (buttons.get(buttons.size() - 1).size() < 3) {
            buttons.get(buttons.size() - 1).add(mixFlavorButton());
        } else {
            buttons.add(List.of(mixFlavorButton()));
        }

        String message = handleBox(chatId);
        sendBotMessageService.sendMessage(chatId, message, buttons);
    }

    private String handleBox(Long chatId) {
        Cart cart = cartService.findCart(chatId);
        String message;
        if (cart.getBoxInProgress() == null) {
            BoxSize boxSize = findNextSize(cart);
            Box boxInProgress = Box.builder()
                    .boxSize(boxSize)
                    .cart(cart)
                    .build();
            cart.setBoxInProgress(boxService.save(boxInProgress));
            cartService.save(cart);

            int boxNUmber = findBoxNumber(cart, boxSize);
            message = String.format(START_BOX_MESSAGE, boxSize.name(), boxNUmber);
        } else {
            Box boxInProgress = cart.getBoxInProgress();
            BoxSize boxSize = boxInProgress.getBoxSize();

            int occupiedNumber = boxInProgress.getFlavors().size();
            int vacantNumber = boxSize.getFlavorNumber() - occupiedNumber;

            int boxNUmber = findBoxNumber(cart, boxSize) + 1;
            message = String.format(IN_PROGRESS_BOX_MESSAGE, boxSize.name(), boxNUmber, occupiedNumber, vacantNumber);
        }
        return message;
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

    private int findBoxNumber(Cart cart, BoxSize boxSize) {
        long thisSizeBoxesNumber = cart.getBoxes().stream().filter(b -> boxSize.equals(b.getBoxSize())).count();
        return (int) thisSizeBoxesNumber + 1;
    }
}
