package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;

@AllArgsConstructor
public class CompleteCartCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CustomerService customerService;
    private final CartService cartService;
    private final BoxService boxService;

    public static final String THANKS_MESSAGE = "Дякуємо за замовлення, наш менеджер скоро звяжеться з вами.";

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        //оновити контакти кастомера
        updateCustomerContacts(update, chatId, username);

        //Надрукувати що в корзині
        StringBuilder message = cartSummery(chatId, username);

        //закрити корзину повідомити адмінів

        sendBotMessageService.sendMessage(getChatId(update), message.toString());
    }

    private StringBuilder cartSummery(Long chatId, String username) {
        Cart cart = cartService.createOrFind(chatId, username);
        Set<Box> boxes = cartService.getBoxesForCart(cart.getId());
        StringBuilder message = new StringBuilder();
        message.append("В твоєму замовленні: \n\n");
        List<String> flavorDescription = boxes.stream().map(boxService::flavorQuantitiesToString).collect(Collectors.toList());
        flavorDescription.forEach(message::append);
        message.append("\n<b>Cпосіб доставки: </b>").append(cart.getDeliveryMethod().getName()).append("\n\n");
        message.append(THANKS_MESSAGE);
        return message;
    }

    private void updateCustomerContacts(Update update, Long chatId, String username) {
        Customer customer = customerService.createOrFind(chatId, username);
        customer.setPhoneNumber(update.getMessage().getContact().getPhoneNumber().trim());
        customer.setFirstName(update.getMessage().getContact().getFirstName());
        customer.setLastName(update.getMessage().getContact().getLastName());
        customerService.saveCustomer(customer);
    }
}
