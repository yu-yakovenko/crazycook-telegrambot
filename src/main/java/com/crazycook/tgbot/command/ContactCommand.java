package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.commentButton;
import static com.crazycook.tgbot.bot.Buttons.completeCartButton;
import static com.crazycook.tgbot.bot.Messages.LEAVE_COMMENT;

@AllArgsConstructor
public class ContactCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CustomerService customerService;

    @Override
    public void execute(Update update) {
        Long customerChatId = getChatId(update);
        String customerUsername = getUserName(update);

        Customer customer = customerService.createOrFind(customerChatId, customerUsername);
        updateCustomerContacts(update, customer);

        sendBotMessageService.sendMessage(customerChatId, LEAVE_COMMENT, List.of(List.of(commentButton(), completeCartButton())));
    }

    private Customer updateCustomerContacts(Update update, Customer customer) {
        customer.setPhoneNumber(update.getMessage().getContact().getPhoneNumber().trim());
        customer.setFirstName(update.getMessage().getContact().getFirstName());
        customer.setLastName(update.getMessage().getContact().getLastName());
        return customerService.saveCustomer(customer);
    }
}
