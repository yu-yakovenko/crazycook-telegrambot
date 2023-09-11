package com.crazycook.tgbot.command.admin;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Admin;
import com.crazycook.tgbot.entity.AdminStatus;
import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.bot.Messages.NEW_FLAVOR_ADDED;

@AllArgsConstructor
public class AddNewFlavorCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final FlavorService flavorService;
    private final AdminService adminService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        if (!adminService.checkIsAdmin(chatId)) {
            return;
        }

        String flavorName = getMessage(update);
        Flavor flavor = Flavor.builder().name(flavorName).isInStock(true).build();
        flavorService.save(flavor);
        Admin admin = adminService.getById(chatId);
        admin.setStatus(AdminStatus.DEFAULT);
        adminService.save(admin);

        sendBotMessageService.sendMessage(chatId, String.format(NEW_FLAVOR_ADDED, flavorName));
    }
}
