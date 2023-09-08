package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.CartStatus;
import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.service.AdminService;
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
    private final AdminService adminService;

    public static final String THANKS_MESSAGE = "\n Дякуємо за замовлення, наш менеджер скоро звяжеться з вами.";

    @Override
    public void execute(Update update) {
        Long customerChatId = getChatId(update);
        String customerUsername = getUserName(update);
        Cart cart = cartService.createOrFind(customerChatId, customerUsername);

        //оновити контакти кастомера
        Customer customer = customerService.createOrFind(customerChatId, customerUsername);
        customer = updateCustomerContacts(update, customer);

        //Надрукувати що в корзині
        String cartSummery = cartSummery(cart);

        //Надіслати повідомлення замовнику
        String messageForCustomer = "В твоєму замовленні: \n\n" + cartSummery + THANKS_MESSAGE;
        sendBotMessageService.sendMessage(customerChatId, messageForCustomer);

        //закрити корзину
        cart.setStatus(CartStatus.DONE);

        // повідомити адмінів
        sendMessageForAdmin(cartSummery, customer);
    }

    private void sendMessageForAdmin(String cartSummery, Customer customer) {
        String message = customer.getFirstName() + " " +
                customer.getLastName() + " " +
                "@" + customer.getUsername() +
                " щойно оформив замовлення. <b>Телефон: " + customer.getPhoneNumber() + ".</b>\n" +
                "\n" + cartSummery;

        Set<Long> adminIds = adminService.getAdminChatIds();
        adminIds.forEach(id -> sendBotMessageService.sendMessage(id, message));
    }


    private String cartSummery(Cart cart) {
        Set<Box> boxes = cartService.getBoxesForCart(cart.getId());
        StringBuilder cartSummery = new StringBuilder();
        List<String> flavorDescription = boxes.stream().map(boxService::flavorQuantitiesToString).collect(Collectors.toList());
        flavorDescription.forEach(cartSummery::append);
        cartSummery.append("\n<b>Cпосіб доставки: </b>").append(cart.getDeliveryMethod().getName()).append("\n");
        return cartSummery.toString();
    }

    private Customer updateCustomerContacts(Update update, Customer customer) {
        customer.setPhoneNumber(update.getMessage().getContact().getPhoneNumber().trim());
        customer.setFirstName(update.getMessage().getContact().getFirstName());
        customer.setLastName(update.getMessage().getContact().getLastName());
        return customerService.saveCustomer(customer);
    }
}
