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
import com.crazycook.tgbot.service.PromoService;
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
import static com.crazycook.tgbot.bot.Messages.ADDRESS;
import static com.crazycook.tgbot.bot.Messages.BOLD_END;
import static com.crazycook.tgbot.bot.Messages.BOLD_START;
import static com.crazycook.tgbot.bot.Messages.COMMENT_IS;
import static com.crazycook.tgbot.bot.Messages.CUSTOMER_JUST_PLACED_ORDER;
import static com.crazycook.tgbot.bot.Messages.DELIVERY_METHOD;
import static com.crazycook.tgbot.bot.Messages.IN_YOUR_ORDER;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.ORDER_EMPTY;
import static com.crazycook.tgbot.bot.Messages.OVERALL_PRICE;
import static com.crazycook.tgbot.bot.Messages.PRICE_WITH_PROMO;
import static com.crazycook.tgbot.bot.Messages.THANKS_MESSAGE;

@AllArgsConstructor
public class CompleteCartCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CustomerService customerService;
    private final CartService cartService;
    private final BoxService boxService;
    private final AdminService adminService;
    private final OrderService orderService;
    private final PromoService promoService;

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
        String address = address(cart);
        String promo = promo(overallPrice, cart);

        if (cartSummery.isBlank()) {
            sendBotMessageService.sendMessage(customerChatId, ORDER_EMPTY, List.of(List.of(addMoreButton())));
            return;
        }

        //Надіслати повідомлення замовнику
        String messageForCustomer = IN_YOUR_ORDER + LINE_END + LINE_END
                + cartSummery + deliveryMethod + address + comment + price + promo + LINE_END + THANKS_MESSAGE;
        sendBotMessageService.sendMessage(customerChatId, messageForCustomer);

        //закрити корзину
        orderService.createOrder(cart);
        cartService.delete(cart);

        // повідомити адмінів
        sendMessageForAdmin(cartSummery + deliveryMethod + address + LINE_END + comment + price + promo, customer);
    }

    private String promo(BigDecimal overallPrice, Cart cart) {
        if (cart.getPromoCode() != null && overallPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal promoPrice = promoService.countPromoPrice(overallPrice, cart.getPromoCode());
            return LINE_END + String.format(PRICE_WITH_PROMO, cart.getPromoCode().getName(), promoPrice);
        }
        return "";
    }

    private String address(Cart cart) {
        if (cart.getAddress() != null && !cart.getAddress().isBlank()) {
            return LINE_END + ADDRESS + cart.getAddress();
        }
        return "";
    }

    private String comment(Cart cart) {
        if (cart.getComment() != null && !cart.getComment().isBlank()) {
            return LINE_END + BOLD_START + COMMENT_IS
                    + BOLD_END + cart.getComment();
        }
        return "";
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
            return LINE_END + BOLD_START + DELIVERY_METHOD + BOLD_END + cart.getDeliveryMethod().getName();
        }
        return "";
    }
}
