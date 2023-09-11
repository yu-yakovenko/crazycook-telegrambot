package com.crazycook.tgbot.command.admin;

import com.crazycook.tgbot.command.CrazyCookTGCommand;
import com.crazycook.tgbot.entity.Admin;
import com.crazycook.tgbot.entity.AdminStatus;
import com.crazycook.tgbot.entity.Promo;
import com.crazycook.tgbot.service.AdminService;
import com.crazycook.tgbot.service.PromoService;
import com.crazycook.tgbot.service.SendBotMessageService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

import static com.crazycook.tgbot.Utils.getChatId;
import static com.crazycook.tgbot.Utils.getMessage;
import static com.crazycook.tgbot.bot.Messages.DATE_WRONG;
import static com.crazycook.tgbot.bot.Messages.NEW_PROMO_ADDED;
import static com.crazycook.tgbot.bot.Messages.PERCENT_WRONG;
import static com.crazycook.tgbot.bot.Messages.PROMO_ALREADY_EXISTS;

@AllArgsConstructor
public class AddNewPromoCommand implements CrazyCookTGCommand {
    private final SendBotMessageService sendBotMessageService;
    private final PromoService promoService;
    private final AdminService adminService;

    @Override
    public void execute(Update update) {
        Long chatId = getChatId(update);
        String message = getMessage(update);
        if (!adminService.checkIsAdmin(chatId)) {
            return;
        }

        String promoName = message.split(";")[0].trim();
        String promoPercent = message.split(";")[1].trim();
        String promoDate = message.split(";")[2].trim();

        Promo promo = parsePromo(promoName, promoPercent, promoDate, chatId);

        if (promo != null) {
            promo = promoService.save(promo);
            Admin admin = adminService.getById(chatId);
            admin.setStatus(AdminStatus.DEFAULT);
            adminService.save(admin);

            sendBotMessageService.sendMessage(chatId, String.format(NEW_PROMO_ADDED, promo.getName(), promo.getPercent(), promo.getExpiringDate()));
        }
    }

    private Promo parsePromo(String promoName, String promoPercent, String promoDate, Long chatId) {
        if (promoService.findByName(promoName).isPresent()) {
            sendBotMessageService.sendMessage(chatId, PROMO_ALREADY_EXISTS);
            return null;
        }
        try {
            Integer percent = Integer.parseInt(promoPercent);
            if (percent < 0 || percent > 100) {
                throw new NumberFormatException("Процент має бути не більше 100 і не меньше 1");
            }
            try {
                LocalDate date = LocalDate.parse(promoDate);
                return Promo.builder().name(promoName).expiringDate(date).percent(percent).build();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                sendBotMessageService.sendMessage(chatId, DATE_WRONG);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            sendBotMessageService.sendMessage(chatId, PERCENT_WRONG);
        }
        return null;
    }
}
