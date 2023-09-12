package com.crazycook.tgbot.command.admin;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Price;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.PriceService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.changePriceIdButton;
import static com.crazycook.tgbot.bot.Messages.BOX_PRICE;
import static com.crazycook.tgbot.bot.Messages.CHANGE_PRICE_MENU;
import static com.crazycook.tgbot.bot.Messages.DELIVERY_PRICE;

@AllArgsConstructor
public class ChangePriceManuCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final PriceService priceService;
    private final AdminService adminService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        if (!adminService.checkIsAdmin(chatId)) {
            return;
        }
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<Price> boxPrices = priceService.getPricesForBoxes();
        boxPrices.forEach(p -> buttons.add(List.of(changePriceIdButton(String.format(BOX_PRICE, p.getName(), p.getValue()), p.getId()))));

        Price deliveryPrice = priceService.getPricesForDelivery();
        buttons.add(List.of(changePriceIdButton(String.format(DELIVERY_PRICE, deliveryPrice.getValue()), deliveryPrice.getId())));

        sendBotMessageService.sendMessage(chatId, CHANGE_PRICE_MENU, buttons);
    }
}
