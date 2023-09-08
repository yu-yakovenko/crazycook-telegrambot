package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.createOrderButton;
import static com.crazycook.tgbot.bot.Buttons.startButton;

@AllArgsConstructor
public class DeliveryCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;

    public final static String PRICE_MESSAGE = """
            Доставка можлива тільки по Києву. 
             🔹 Доставка кур'єром. Після оформлення замовлення ми вам передзвонимо для уточння часу і місця\s
             🔹 Самовивоз. Сікорського 1, з 10 до 20 за попередньою домовленістю\s
             🔹 Новою поштою не відправляємо, бо макарнчики надто тендітні і не перживають таку доставку.\s
            """;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);

        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        buttonRow.add(startButton());
        buttonRow.add(createOrderButton());

        sendBotMessageService.sendMessage(chatId, PRICE_MESSAGE, List.of(buttonRow));
    }
}
