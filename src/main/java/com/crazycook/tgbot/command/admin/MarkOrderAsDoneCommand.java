package com.crazycook.tgbot.command.admin;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Order;
import com.crazycook.tgbot.entity.OrderStatus;
import com.crazycook.tgbot.service.OrderService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.bot.Messages.ORDER_MARKED_AS_DONE;
import static com.crazycook.tgbot.bot.Messages.ORDER_NOT_FIND;

@AllArgsConstructor
public class MarkOrderAsDoneCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final OrderService orderService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String message = getMessage(update);
        String orderId = message.split(" ")[1];

        Optional<Order> optOrder = orderService.findById(Long.parseLong(orderId));
        if (optOrder.isPresent()) {
            Order order = optOrder.get();
            order.setStatus(OrderStatus.DONE);
            orderService.save(order);
            sendBotMessageService.sendMessage(chatId, String.format(ORDER_MARKED_AS_DONE, orderId));
        } else {
            sendBotMessageService.sendMessage(chatId, String.format(ORDER_NOT_FIND, orderId));
        }
    }

}
