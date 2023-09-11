package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Admin;
import com.crazycook.tgbot.entity.AdminStatus;
import com.crazycook.tgbot.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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

    public boolean checkIsAdmin(Long chatId) {
        return adminRepository.findById(chatId).isPresent();
    }

    public boolean isHasStatus(Long chatId, AdminStatus status) {
        Optional<Admin> optAdmin = adminRepository.findById(chatId);
        if (optAdmin.isPresent()) {
            Admin admin = optAdmin.get();
            return status.equals(admin.getStatus());
        }
        return false;
    }

    public Admin getById(Long chatId) {
        Admin admin = adminRepository.getById(chatId);
        admin.getStatus(); // the only way to avoid could not initialize proxy - no Session
        return admin;
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }
}
