package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.service.CartService;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.cartCompleteButtons;
import static com.crazycook.tgbot.bot.Messages.IN_YOUR_ORDER;
import static com.crazycook.tgbot.bot.Messages.LEAVE_COMMENT;
import static com.crazycook.tgbot.bot.Messages.LINE_END;

@AllArgsConstructor
public class ContactCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CustomerService customerService;
    private final CartService cartService;

    @Override
    public void execute(Update update) {
        Long customerChatId = getChatId(update);
        String customerUsername = getUserName(update);
        Cart cart = cartService.findCart(customerChatId, customerUsername);

        Customer customer = customerService.createOrFind(customerChatId, customerUsername);
        updateCustomerContacts(update, customer);
        List<List<InlineKeyboardButton>> buttons = cartCompleteButtons(cartService.readyForComplete(cart));
        String message = IN_YOUR_ORDER + LINE_END + LINE_END + cartService.cartSummery(cart) + LINE_END + LINE_END + LEAVE_COMMENT;

        sendBotMessageService.sendMessage(customerChatId, message, buttons);
    }

    private Customer updateCustomerContacts(Update update, Customer customer) {
        customer.setPhoneNumber(update.getMessage().getContact().getPhoneNumber().trim());
        customer.setFirstName(update.getMessage().getContact().getFirstName());
        customer.setLastName(update.getMessage().getContact().getLastName());
        return customerService.saveCustomer(customer);
    }
}
