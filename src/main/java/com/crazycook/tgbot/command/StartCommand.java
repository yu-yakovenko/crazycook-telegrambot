package com.crazycook.tgbot.command;

import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.CustomerService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getUserName;
import static com.crazycook.tgbot.bot.Buttons.adminMainMenuButtons;
import static com.crazycook.tgbot.bot.Buttons.mainMenuButtons;
import static com.crazycook.tgbot.bot.Messages.START_ADMIN_MESSAGE;
import static com.crazycook.tgbot.bot.Messages.START_USER_MESSAGE;

@AllArgsConstructor
public class StartCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final CustomerService customerService;
    private final AdminService adminService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String username = getUserName(update);
        customerService.createOrFind(chatId, username);
        boolean isAdmin = adminService.checkIsAdmin(chatId);
        if (isAdmin) {
            sendBotMessageService.sendMessage(chatId, START_ADMIN_MESSAGE, adminMainMenuButtons());
        } else {
            sendBotMessageService.sendMessage(chatId, START_USER_MESSAGE, mainMenuButtons());
        }
    }
}
