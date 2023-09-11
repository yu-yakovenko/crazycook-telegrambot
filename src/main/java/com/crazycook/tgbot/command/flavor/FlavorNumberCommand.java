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

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.addMoreButton;
import static com.crazycook.tgbot.bot.Buttons.chooseDeliveryButton;
import static com.crazycook.tgbot.bot.Buttons.generateFlavorButtons;
import static com.crazycook.tgbot.bot.Buttons.mixFlavorForAllButton;
import static com.crazycook.tgbot.bot.Buttons.nextBoxButton;
import static com.crazycook.tgbot.bot.Buttons.promoCodeButton;
import static com.crazycook.tgbot.bot.Buttons.showCartButton;
import static com.crazycook.tgbot.bot.Messages.BOX_COMPLETE;
import static com.crazycook.tgbot.bot.Messages.CART_COMPLETE;
import static com.crazycook.tgbot.entity.CartStatus.IN_PROGRESS;

@AllArgsConstructor
public class FlavorNumberCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final FlavorService flavorService;
    private final CartService cartService;
    private final BoxService boxService;
    private final FlavorQuantityService flavorQuantityService;

    public final static String BOX_OVERFLOW = "Упс, схоже що в цей бокс ще %s макарончиків не влізе.\n";
    public static final String MESSAGE = "Ми додали %d макарон зі смаком %s в %d-й %s бокс. ";
    public static final String MORE_FLAVORS_POSSIBLE = "Ще можна додати %d макарон. Обери наступний смак:";

    @Override
    @Transactional
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);

        int number = Integer.parseInt(Utils.getMessage(update));
        String message = "";

        Cart cart = cartService.createOrFind(chatId, username);
        Flavor flavor = cart.getCurrentFlavor();
        Box boxInProgress = cart.getBoxInProgress();

        BoxSize boxSize = boxInProgress.getBoxSize();
        List<FlavorQuantity> flavorQuantities = boxService.getFlavorQuantitiesForBox(boxInProgress.getId());

        int occupiedNumber = flavorQuantities.stream().map(FlavorQuantity::getQuantity).reduce(Integer::sum).orElse(0);
        int vacantNumber = boxSize.getCapacity() - occupiedNumber;

        int boxIndex = cartService.findCurrentBoxIndex(cart, boxSize);

        boolean moreFlavorsPossible = true;
        boolean moreBoxesPossible = cartService.isMoreBoxesPossible(cart);

        if (vacantNumber < number) {
            message += String.format(BOX_OVERFLOW, number);
            number = vacantNumber;
            moreFlavorsPossible = false;
        } else if (vacantNumber == number) {
            moreFlavorsPossible = false;
        }

        addFlavorToFlavorQuantities(flavor, number, flavorQuantities, boxInProgress);

        cart.setStatus(IN_PROGRESS);
        cart.setCurrentFlavor(null);
        boxInProgress.setFlavorQuantities(flavorQuantities);
        cart.setBoxInProgress(boxService.save(boxInProgress));
        cartService.save(cart);

        if (!moreFlavorsPossible) {
            cartService.completeBoxInProgress(cart);
        }

        message += String.format(MESSAGE, number, flavor.getName(), boxIndex, boxSize.name());

        List<List<InlineKeyboardButton>> buttons;
        if (moreFlavorsPossible) {
            message += String.format(MORE_FLAVORS_POSSIBLE, vacantNumber - number);
            buttons = generateFlavorButtons(flavorService.getAllInStock().stream().toList());
        } else if (moreBoxesPossible) {
            //Показати повідомлення що бокс повністю заповнено
            message += BOX_COMPLETE;
            //Показати кнопу "перейти до заповнення нового боксу", "Для всіх інших боксів зробіть мікс смаків" та "показати що в корзині"
            buttons = List.of(List.of(nextBoxButton()), List.of(mixFlavorForAllButton()), List.of(showCartButton()));
        } else {
            //Показати повідомлення про те, що корзина повністю заповнена
            message += CART_COMPLETE;
            //Показати кнопки "показати що в корзині", "додати ще бокси" та "вибрати спосіб доставки"
            buttons = List.of(List.of(addMoreButton(), showCartButton()), List.of(chooseDeliveryButton(), promoCodeButton()));
        }

        sendBotMessageService.sendMessage(chatId, message, buttons);
    }

    private void addFlavorToFlavorQuantities(Flavor flavor, int number, List<FlavorQuantity> flavorQuantities, Box box) {
        Optional<FlavorQuantity> optFq = flavorQuantities.stream().filter(f -> f.getFlavor().getId().equals(flavor.getId())).findFirst();
        if (optFq.isPresent()) {
            FlavorQuantity fq = optFq.get();
            fq.setQuantity(fq.getQuantity() + number);
            flavorQuantityService.save(fq);
        } else {
            FlavorQuantity newFq = FlavorQuantity.builder().flavor(flavor).quantity(number).box(box).build();
            flavorQuantityService.save(newFq);
            flavorQuantities.add(newFq);
        }
    }
}
