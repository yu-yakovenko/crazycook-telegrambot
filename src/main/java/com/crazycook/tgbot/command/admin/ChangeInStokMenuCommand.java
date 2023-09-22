package com.crazycook.tgbot.command.admin;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Set;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.generateChangeFlavorButtons;
import static com.crazycook.tgbot.bot.Messages.IN_STOCK_FLAVORS;
import static com.crazycook.tgbot.bot.Messages.NOT_IN_STOCK_FLAVORS;

@AllArgsConstructor
public class ChangeInStokMenuCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final FlavorService flavorService;
    private final AdminService adminService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        if (!adminService.checkIsAdmin(chatId)) {
            return;
        }

        Set<Flavor> inStock = flavorService.getAllInStock();
        if (inStock.size() > 0) {
            List<List<InlineKeyboardButton>> inStockButtons = generateChangeFlavorButtons(inStock.stream().toList());
            sendBotMessageService.sendMessage(chatId, IN_STOCK_FLAVORS, inStockButtons);
        }

        Set<Flavor> notInStock = flavorService.getAllNotInStock();
        if (notInStock.size() > 0) {
            List<List<InlineKeyboardButton>> notInStockButtons = generateChangeFlavorButtons(notInStock.stream().toList());
            sendBotMessageService.sendMessage(chatId, NOT_IN_STOCK_FLAVORS, notInStockButtons);
        }
    }
}
