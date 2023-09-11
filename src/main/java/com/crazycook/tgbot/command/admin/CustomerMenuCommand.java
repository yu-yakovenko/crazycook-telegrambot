package com.crazycook.tgbot.command.admin;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Buttons.customerMenuButtons;
import static com.crazycook.tgbot.bot.Messages.CUSTOMER_MENU;

@AllArgsConstructor
public class CustomerMenuCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final AdminService adminService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        if (!adminService.checkIsAdmin(chatId)) {
            return;
        }

        sendBotMessageService.sendMessage(chatId, CUSTOMER_MENU, customerMenuButtons());
    }
}
