package com.crazycook.tgbot.command.box;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Set;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.PlUS_ONE;
import static com.crazycook.tgbot.bot.Buttons.boxSizeButtons;
import static com.crazycook.tgbot.bot.Buttons.chooseFlavorsButton;
import static com.crazycook.tgbot.bot.Messages.BOX_ADDED;
import static com.crazycook.tgbot.bot.Messages.IN_YOUR_CART;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.ONE_MORE_BOX_ADDED;
import static com.crazycook.tgbot.bot.Messages.ONE_MORE_BOX_REMOVED;
import static com.crazycook.tgbot.entity.BoxSize.L;
import static com.crazycook.tgbot.entity.BoxSize.M;
import static com.crazycook.tgbot.entity.BoxSize.S;
import static com.crazycook.tgbot.entity.CartStatus.IN_PROGRESS;

@AllArgsConstructor
public class AddBoxCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CartService cartService;
    private final BoxService boxService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        String callback = getMessage(update);

        Cart cart = cartService.createOrFind(chatId, username);
        Set<Box> boxes = cartService.getBoxesForCart(cart.getId());

        BoxSize size = BoxSize.valueOf(callback.split(" ")[1].trim());
        String sing = callback.split(" ")[2].trim();
        Boolean isAdding = PlUS_ONE.equals(sing);

        if (isAdding) {
            plusBox(cart, size);
        } else {
            minusBox(cart, boxes, size);
        }

        List<List<InlineKeyboardButton>> buttons = boxSizeButtons();
        if (cart.getSNumber() + cart.getMNumber() + cart.getLNumber() > 0) {
            buttons.add(List.of(chooseFlavorsButton()));
        }
        cart.setStatus(IN_PROGRESS);
        cartService.save(cart);
        sendBotMessageService.editMessage(update.getCallbackQuery().getMessage().getMessageId(),
                chatId,
                message(cart, size, isAdding),
                buttons);
    }

    private void minusBox(Cart cart, Set<Box> boxes, BoxSize size) {
        long filledSNumber = boxes.stream().filter(b -> S.equals(b.getBoxSize())).count();
        long filledMNumber = boxes.stream().filter(b -> M.equals(b.getBoxSize())).count();
        long filledLNumber = boxes.stream().filter(b -> L.equals(b.getBoxSize())).count();
        switch (size) {
            case S -> {
                if (cart.getSNumber() > 0) {
                    cart.setSNumber(cart.getSNumber() - 1);
                }
                if (cart.getSNumber() < filledSNumber) {
                    removeBox(boxes, S);
                }
            }
            case M -> {
                if (cart.getMNumber() > 0) {
                    cart.setMNumber(cart.getMNumber() - 1);
                }
                if (cart.getMNumber() < filledMNumber) {
                    removeBox(boxes, M);
                }
            }
            case L -> {
                if (cart.getLNumber() > 0) {
                    cart.setLNumber(cart.getLNumber() - 1);
                }
                if (cart.getLNumber() < filledLNumber) {
                    removeBox(boxes, L);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + cart.getStatus());
        }
    }

    private void removeBox(Set<Box> boxes, BoxSize size) {
        boxes.stream().filter(b -> size.equals(b.getBoxSize())).findFirst().ifPresent(boxService::delete);
    }

    private void plusBox(Cart cart, BoxSize size) {
        switch (size) {
            case S -> cart.setSNumber(cart.getSNumber() + 1);
            case M -> cart.setMNumber(cart.getMNumber() + 1);
            case L -> cart.setLNumber(cart.getLNumber() + 1);
            default -> throw new IllegalStateException("Unexpected value: " + cart.getStatus());
        }
    }

    private String message(Cart cart, BoxSize size, Boolean isAdding) {
        String message = "";
        if (cart.getSNumber() + cart.getMNumber() + cart.getLNumber() == 1 && isAdding) {
            message += String.format(BOX_ADDED, size.name());
        } else if (isAdding) {
            message += String.format(ONE_MORE_BOX_ADDED, size.name());
        } else {
            message += String.format(ONE_MORE_BOX_REMOVED, size.name());
        }
        return message + LINE_END + LINE_END + IN_YOUR_CART + cartService.cartBoxesToString(cart);
    }
}
