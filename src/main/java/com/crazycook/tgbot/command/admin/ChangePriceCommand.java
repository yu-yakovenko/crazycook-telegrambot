package com.crazycook.tgbot.command.admin;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Admin;
import com.crazycook.tgbot.entity.AdminStatus;
import com.crazycook.tgbot.entity.Price;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.PriceService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.bot.Messages.INVALID_PRICE_VALUE;
import static com.crazycook.tgbot.bot.Messages.PRICE_WAS_CHAGED;

@AllArgsConstructor
public class ChangePriceCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final PriceService priceService;
    private final AdminService adminService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        if (!adminService.checkIsAdmin(chatId)) {
            return;
        }
        Admin admin = adminService.getById(chatId);
        String strPrice = getMessage(update).trim();
        Long priceId = admin.getIdToChange();

        try {
            double newPrice = Double.parseDouble(strPrice);
            Price price = priceService.findById(priceId);
            price.setValue(new BigDecimal(newPrice));
            priceService.save(price);
            admin.setStatus(AdminStatus.DEFAULT);
            admin.setIdToChange(null);
            adminService.save(admin);

            sendBotMessageService.sendMessage(chatId, PRICE_WAS_CHAGED);
        } catch (NumberFormatException e) {
            sendBotMessageService.sendMessage(chatId, INVALID_PRICE_VALUE);
        }
    }
}