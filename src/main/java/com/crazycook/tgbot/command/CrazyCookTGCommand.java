package com.crazycook.tgbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CrazyCookTGCommand {

    void execute(Update update);
}
