package com.crazycook.tgbot.command.admin;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Flavor;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.FlavorService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.bot.Messages.IN_STOCK_WAS_CHANGED;

@AllArgsConstructor
public class ChangeInStokCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final FlavorService flavorService;
    private final AdminService adminService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String message = getMessage(update);
        if (!adminService.checkIsAdmin(chatId)) {
            return;
        }

        String id = message.split(" ")[1].trim();
        Flavor flavor = flavorService.getById(id);
        flavor.setIsInStock(!flavor.getIsInStock());
        flavorService.save(flavor);
        String status = flavor.getIsInStock() ? "'в наявності'" : "'немає в наявності'";
        sendBotMessageService.sendMessage(chatId, String.format(IN_STOCK_WAS_CHANGED, flavor.getName(), status));
    }
}
