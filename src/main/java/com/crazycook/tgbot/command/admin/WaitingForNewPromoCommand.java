package com.crazycook.tgbot.command.admin;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Admin;
import com.crazycook.tgbot.entity.AdminStatus;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.bot.Messages.INPUT_NEW_PROMO;

@AllArgsConstructor
public class WaitingForNewPromoCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final AdminService adminService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        if (!adminService.checkIsAdmin(chatId)) {
            return;
        }
        Admin admin = adminService.getById(chatId);
        admin.setStatus(AdminStatus.WAITING_FOR_PROMO);
        adminService.save(admin);

        sendBotMessageService.sendMessage(chatId, INPUT_NEW_PROMO);
    }
}
