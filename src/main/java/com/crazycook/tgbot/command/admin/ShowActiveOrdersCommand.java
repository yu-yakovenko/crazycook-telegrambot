package com.crazycook.tgbot.command.admin;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.entity.Order;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.BoxService;
import com.crazycook.tgbot.service.OrderService;
import com.crazycook.tgbot.service.PromoService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.setOrderAsDoneButton;
import static com.crazycook.tgbot.bot.Messages.ADDRESS;
import static com.crazycook.tgbot.bot.Messages.BOLD_END;
import static com.crazycook.tgbot.bot.Messages.BOLD_START;
import static com.crazycook.tgbot.bot.Messages.COMMENT;
import static com.crazycook.tgbot.bot.Messages.CUSTOMER_CONTACT;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.ORDER_NUMBER;
import static com.crazycook.tgbot.bot.Messages.OVERALL_PRICE;
import static com.crazycook.tgbot.bot.Messages.PRICE_WITH_PROMO;

@AllArgsConstructor
public class ShowActiveOrdersCommand implements CrazyCookTGCommand {

    private final SendBotMessageService sendBotMessageService;
    private final OrderService orderService;
    private final BoxService boxService;
    private final AdminService adminService;
    private final PromoService promoService;

    @Override
    @Transactional
    public void execute(Update update) {
        Long chatId = getChatId(update);
        if (!adminService.checkIsAdmin(chatId)) {
            return;
        }

        List<Order> orders = orderService.findAllActiveOrders();
        orders.forEach(o -> printOrderSummery(chatId, o));
    }

    private void printOrderSummery(Long chatId, Order order) {
        StringBuilder message = new StringBuilder(ORDER_NUMBER + order.getId() + LINE_END);

        addCustomerContact(order, message);

        addBoxDescription(order, message);
        addComment(order, message);

        BigDecimal overallPrice = orderService.countOverallPrice(order);
        addOverallPrice(overallPrice, message);
        addPromo(overallPrice, order, message);

        addAddress(order, message);

        sendBotMessageService.sendMessage(chatId, message.toString(), List.of(List.of(setOrderAsDoneButton(order.getId()))));
    }

    private void addCustomerContact(Order order, StringBuilder sb) {
        Customer customer = order.getCustomer();
        String lastName = customer.getLastName() == null ? "" : customer.getLastName();
        sb.append(String.format(CUSTOMER_CONTACT,
                customer.getFirstName(),
                lastName,
                customer.getUsername(), customer.getPhoneNumber()));
    }

    private void addComment(Order order, StringBuilder sb) {
        if (order.getComment() != null && !order.getComment().isBlank()) {
            sb.append(LINE_END).append(BOLD_START).append(COMMENT).append(BOLD_END).append(order.getComment());
        }
    }

    private void addAddress(Order order, StringBuilder sb) {
        if (order.getAddress() != null && !order.getAddress().isBlank()) {
            sb.append(LINE_END).append(ADDRESS).append(order.getAddress());
        }
    }

    private void addOverallPrice(BigDecimal overallPrice, StringBuilder sb) {
        sb.append(LINE_END).append(String.format(OVERALL_PRICE, overallPrice));
    }

    private void addPromo(BigDecimal overallPrice, Order order, StringBuilder sb) {
        boolean hasPromo = order.getPromoCode() != null;
        if (hasPromo && overallPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal promoPrice = promoService.countPromoPrice(overallPrice, order.getPromoCode());
            sb.append(LINE_END).append(String.format(PRICE_WITH_PROMO, order.getPromoCode().getName(), promoPrice));
        }
    }

    private void addBoxDescription(Order order, StringBuilder sb) {
        sb.append(orderService.flavorMixToString(order));

        Set<Box> boxes = orderService.getBoxesForOrder(order.getId());
        List<String> flavorDescription = boxes.stream().map(boxService::flavorQuantitiesToString).collect(Collectors.toList());
        for (String s : flavorDescription) {
            sb.append(s);
        }
    }
}
