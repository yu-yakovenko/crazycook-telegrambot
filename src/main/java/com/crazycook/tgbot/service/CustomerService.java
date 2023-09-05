package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer createOrFind(Long chatId) {
        return createOrFind(chatId, null);
    }

    public Customer createOrFind(Long chatId, String username) {
        Customer customer = getCustomerById(chatId);
        if (customer == null) {
            return customerRepository.save(Customer.builder()
                    .chatId(chatId)
                    .username(username)
                    .build());
        }
        return customer;
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

}