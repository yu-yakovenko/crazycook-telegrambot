package com.crazycook.tgbot.command.cart;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.OrderService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Set;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.addMoreButton;
import static com.crazycook.tgbot.bot.Messages.CUSTOMER_JUST_PLACED_ORDER;
import static com.crazycook.tgbot.bot.Messages.IN_YOUR_ORDER;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.ORDER_EMPTY;
import static com.crazycook.tgbot.bot.Messages.THANKS_MESSAGE;

@AllArgsConstructor
public class CompleteCartCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CustomerService customerService;
    private final CartService cartService;
    private final AdminService adminService;
    private final OrderService orderService;

    @Override
    public void execute(Update update) {
        Long customerChatId = getChatId(update);
        String customerUsername = getUserName(update);
        Cart cart = cartService.createOrFind(customerChatId, customerUsername);
        Customer customer = customerService.createOrFind(customerChatId, customerUsername);

        //Надрукувати що в корзині
        String cartSummery = cartService.cartSummery(cart);

        if (cartSummery.isBlank()) {
            sendBotMessageService.sendMessage(customerChatId, ORDER_EMPTY, List.of(List.of(addMoreButton())));
            return;
        }

        //Надіслати повідомлення замовнику
        String messageForCustomer = IN_YOUR_ORDER + LINE_END + LINE_END
                + cartSummery + LINE_END + LINE_END + THANKS_MESSAGE;
        sendBotMessageService.sendMessage(customerChatId, messageForCustomer);

        //закрити корзину
        orderService.createOrder(cart);
        cartService.delete(cart);

        // повідомити адмінів
        sendMessageForAdmin(cartSummery + LINE_END, customer);
    }

    private void sendMessageForAdmin(String cartSummery, Customer customer) {
        String lastName = customer.getLastName() == null ? "" : customer.getLastName();

        String message = String.format(CUSTOMER_JUST_PLACED_ORDER,
                customer.getFirstName(),
                lastName,
                customer.getUsername(), customer.getPhoneNumber())
                + cartSummery;

        Set<Long> adminIds = adminService.getAdminChatIds();
        adminIds.forEach(id -> sendBotMessageService.sendMessage(id, message));
    }

}
