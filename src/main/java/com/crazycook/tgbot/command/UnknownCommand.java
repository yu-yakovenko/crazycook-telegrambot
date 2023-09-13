package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.chooseBoxButton;
import static com.crazycook.tgbot.bot.Buttons.deliveryButton;
import static com.crazycook.tgbot.bot.Buttons.flavorsButton;
import static com.crazycook.tgbot.bot.Buttons.priceButton;
import static com.crazycook.tgbot.bot.Buttons.showCartButton;

@AllArgsConstructor
public class UnknownCommand implements CrazyCookTGCommand {
    public static final String UNKNOWN_MESSAGE = "Вибач, я не розумію, твоє повідомлення \uD83E\uDD37\u200D♂️. Ось головне меню, обери що тебе цікавить:";

    private final SendBotMessageService sendBotMessageService;

    @Override
    public void execute(Update update) {
        List<InlineKeyboardButton> buttonRow1 = List.of(flavorsButton(), chooseBoxButton());
        List<InlineKeyboardButton> buttonRow2 = List.of(priceButton(), deliveryButton());
        List<InlineKeyboardButton> buttonRow3 = List.of(showCartButton());

        sendBotMessageService.sendMessage(getChatId(update), UNKNOWN_MESSAGE, List.of(buttonRow1, buttonRow2, buttonRow3));
    }
}
