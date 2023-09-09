package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Admin;
import com.crazycook.tgbot.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    public Set<Long> getAdminChatIds() {
        return adminRepository.findAll().stream().map(Admin::getChatId).collect(Collectors.toSet());
    }

    public Set<String> getAdminUsernames() {
        return adminRepository.findAll().stream().map(Admin::getUsername).collect(Collectors.toSet());
    }
}
