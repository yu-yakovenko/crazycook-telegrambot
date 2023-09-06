package com.crazycook.tgbot.command;

import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.flavorIdButton;

@AllArgsConstructor
public class ChooseFlavorsCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final FlavorService flavorService;

    public final static String CHOOSE_FLAVOR_MESSAGE = "<b>Додай смак до боксу: </b>\n";

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);

        List<Flavor> flavors = flavorService.getAllInStock().stream().toList();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int rowsNumber = flavors.size() % 3 + 1;
        int flavorIndex = 0;
        for (int i = 0; i < rowsNumber; i++) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            for (int j = 0; j < 3 && flavorIndex < flavors.size(); j++, flavorIndex++) {
                buttonRow.add(flavorIdButton(flavors.get(flavorIndex).getName(), flavors.get(flavorIndex).getId()));
            }
            buttons.add(buttonRow);
        }

        sendBotMessageService.sendMessage(chatId, CHOOSE_FLAVOR_MESSAGE, buttons);
    }
}
