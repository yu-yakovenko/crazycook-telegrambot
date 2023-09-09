package com.crazycook.tgbot.command.delivery;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.courierButton;
import static com.crazycook.tgbot.bot.Buttons.selfPickupButton;

@AllArgsConstructor
public class ChooseDeliveryCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;

    public final static String DELIVERY_MESSAGE = """
            Доставка можлива тільки по Києву.
             🔹 Доставка кур'єром. Після оформлення замовлення ми вам передзвонимо для уточння часу і місця\s
             🔹 Самовивоз. Сікорського 1, з 10 до 20 за попередньою домовленістю\s
             🔹 Новою поштою не відправляємо, бо макарнчики надто тендітні і не перживають таку доставку.\s
            """;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        sendBotMessageService.sendMessage(chatId, DELIVERY_MESSAGE,
                List.of(List.of(courierButton(), selfPickupButton())));
    }
}
