package com.crazycook.tgbot.command.cart;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.OrderService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.addMoreButton;
import static com.crazycook.tgbot.bot.Messages.BOLD_END;
import static com.crazycook.tgbot.bot.Messages.BOLD_START;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.OVERALL_PRICE;
import static com.crazycook.tgbot.bot.Messages.THANKS_MESSAGE;

@AllArgsConstructor
public class CompleteCartCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CustomerService customerService;
    private final CartService cartService;
    private final BoxService boxService;
    private final AdminService adminService;
    private final OrderService orderService;

    @Override
    public void execute(Update update) {
        Long customerChatId = getChatId(update);
        String customerUsername = getUserName(update);
        Cart cart = cartService.createOrFind(customerChatId, customerUsername);
        Customer customer = customerService.createOrFind(customerChatId, customerUsername);

        //Надрукувати що в корзині
        String cartSummery = cartSummery(cart);
        String deliveryMethod = delivery(cart);
        String comment = comment(cart);
        BigDecimal overallPrice = cartService.countOverallPrice(cart);
        String price = LINE_END + String.format(OVERALL_PRICE, overallPrice) + LINE_END;

        if (cartSummery.isBlank()) {
            String messageForCustomer = "В твоєму замовленні ще нічого немає.";
            sendBotMessageService.sendMessage(customerChatId, messageForCustomer, List.of(List.of(addMoreButton())));
            return;
        }

        //Надіслати повідомлення замовнику
        String messageForCustomer = "В твоєму замовленні: " + LINE_END + LINE_END
                + cartSummery + deliveryMethod + comment + price + LINE_END + THANKS_MESSAGE;
        sendBotMessageService.sendMessage(customerChatId, messageForCustomer);

        //закрити корзину
        orderService.createOrder(cart);
        cartService.delete(cart);

        // повідомити адмінів
        sendMessageForAdmin(cartSummery + deliveryMethod + comment + price, customer);
    }

    private String comment(Cart cart) {
        if (cart.getComment() != null && !cart.getComment().isBlank()) {
            return BOLD_START + "Коментар: " + BOLD_END + cart.getComment();
        }
        return "";
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
        cartSummery.append(cartService.flavorMixToString(cart));
        List<String> flavorDescription = boxes.stream().map(boxService::flavorQuantitiesToString).collect(Collectors.toList());
        flavorDescription.forEach(cartSummery::append);
        return cartSummery.toString();
    }

    private String delivery(Cart cart) {
        if (cart.getDeliveryMethod() != null) {
            return "\n<b>Cпосіб доставки: </b>" + cart.getDeliveryMethod().getName() + "\n";
        }
        return "";
    }
}
