package com.crazycook.tgbot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:bot.properties")
@Data
public class AppProperty {
    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;
}
