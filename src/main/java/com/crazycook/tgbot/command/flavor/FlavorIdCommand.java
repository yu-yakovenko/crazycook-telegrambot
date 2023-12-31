package com.crazycook.tgbot.command.flavor;

import com.crazycook.tgbot.Utils;
import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.entity.FlavorQuantity;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.FlavorQuantityService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.PlUS_ONE;
import static com.crazycook.tgbot.bot.Buttons.addMoreButton;
import static com.crazycook.tgbot.bot.Buttons.chooseDeliveryButton;
import static com.crazycook.tgbot.bot.Buttons.minusFlavorIdButton;
import static com.crazycook.tgbot.bot.Buttons.moreBoxesPossibleButtons;
import static com.crazycook.tgbot.bot.Buttons.plusFlavorIdButton;
import static com.crazycook.tgbot.bot.Buttons.refreshCartButton;
import static com.crazycook.tgbot.bot.Buttons.showCartButton;
import static com.crazycook.tgbot.bot.Messages.BOX_COMPLETE;
import static com.crazycook.tgbot.bot.Messages.CART_COMPLETE;
import static com.crazycook.tgbot.bot.Messages.IN_PROGRESS_BOX_MESSAGE;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.moreFlavorsPossible;
import static com.crazycook.tgbot.entity.CartStatus.IN_PROGRESS;

@AllArgsConstructor
public class FlavorIdCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final FlavorService flavorService;
    private final CartService cartService;
    private final BoxService boxService;
    private final FlavorQuantityService flavorQuantityService;

    @Transactional
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        String callBackData = Utils.getMessage(update);
        String flavorId = callBackData.split(" ")[1].toLowerCase();
        String sign = callBackData.split(" ")[2].toLowerCase();

        String message = "";

        Cart cart = cartService.createOrFind(chatId, username);
        Flavor flavor = flavorService.getById(flavorId);
        Box boxInProgress = cart.getBoxInProgress();

        BoxSize boxSize = boxInProgress.getBoxSize();
        List<FlavorQuantity> flavorQuantities = boxService.getFlavorQuantitiesForBox(boxInProgress.getId());

        int occupiedNumber = flavorQuantities.stream().map(FlavorQuantity::getQuantity).reduce(Integer::sum).orElse(0);
        int vacantNumber = boxSize.getCapacity() - occupiedNumber;

        int boxIndex = cartService.findCurrentBoxIndex(cart, boxSize);

        boolean moreFlavorsPossible = true;
        boolean moreBoxesPossible = cartService.isMoreBoxesPossible(cart);

        if (vacantNumber == 1 && PlUS_ONE.equals(sign)) {
            moreFlavorsPossible = false;
        }

        if (PlUS_ONE.equals(sign)) {
            addFlavorToFlavorQuantities(flavor, flavorQuantities, boxInProgress);
            vacantNumber -= 1;
        } else {
            minusFlavorToFlavorQuantities(flavor, flavorQuantities, boxInProgress);
            vacantNumber += 1;
        }

        if (moreFlavorsPossible) {
            message += String.format(IN_PROGRESS_BOX_MESSAGE, boxSize.name(), boxIndex);
        } else {
            cart.setBoxInProgress(null);
            message += String.format(BOX_COMPLETE, boxIndex, boxSize.name());
        }

        cart.setStatus(IN_PROGRESS);
        boxService.save(boxInProgress);
        cartService.save(cart);

        message += LINE_END + boxService.flavorQuantitiesToString(boxInProgress);

        List<List<InlineKeyboardButton>> buttons;
        if (moreFlavorsPossible) {
            message += moreFlavorsPossible(vacantNumber);
            buttons = flavorService.getAllInStock().stream()
                    .map(f -> List.of(plusFlavorIdButton(f.getId(), f.getName()), minusFlavorIdButton(f.getId())))
                    .collect(Collectors.toList());
        } else if (moreBoxesPossible) {
            //Показати кнопу "перейти до заповнення нового боксу", "Для всіх інших боксів зробіть мікс смаків" та "показати що в корзині"
            buttons = moreBoxesPossibleButtons();
        } else {
            //Показати кнопки "показати що в корзині", "додати ще бокси" та "вибрати спосіб доставки"
            message += CART_COMPLETE;
            buttons = List.of(List.of(chooseDeliveryButton()), List.of(addMoreButton()), List.of(showCartButton(), refreshCartButton()));
        }

        sendBotMessageService.editMessage(update.getCallbackQuery().getMessage().getMessageId(), chatId, message, buttons);
    }

    private void minusFlavorToFlavorQuantities(Flavor flavor, List<FlavorQuantity> flavorQuantities, Box box) {
        Optional<FlavorQuantity> optFq = flavorQuantities.stream().filter(f -> f.getFlavor().getId().equals(flavor.getId())).findFirst();
        if (optFq.isPresent()) {
            FlavorQuantity fq = optFq.get();
            if (fq.getQuantity() <= 1) {
                flavorQuantities.remove(fq);
                box.setFlavorQuantities(flavorQuantities);
            } else {
                fq.setQuantity(fq.getQuantity() - 1);
                flavorQuantityService.save(fq);
            }
        }
    }

    private void addFlavorToFlavorQuantities(Flavor flavor, List<FlavorQuantity> flavorQuantities, Box box) {
        Optional<FlavorQuantity> optFq = flavorQuantities.stream().filter(f -> f.getFlavor().getId().equals(flavor.getId())).findFirst();
        if (optFq.isPresent()) {
            FlavorQuantity fq = optFq.get();
            fq.setQuantity(fq.getQuantity() + 1);
            flavorQuantityService.save(fq);
        } else {
            FlavorQuantity newFq = FlavorQuantity.builder().flavor(flavor).quantity(1).box(box).build();
            flavorQuantityService.save(newFq);
            flavorQuantities.add(newFq);
            box.setFlavorQuantities(flavorQuantities);
        }
    }
}
