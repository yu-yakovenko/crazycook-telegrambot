package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.CartStatus;
import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerService customerService;

    public Cart createOrFind(Long chatId) {
        return createOrFind(customerService.createOrFind(chatId));
    }

    public Cart createOrFind(Customer customer) {
        Cart cart = cartRepository.findByCustomer(customer);
        if (cart == null) {
            cart = cartRepository.save(Cart.builder()
                    .customer(customer)
                    .status(CartStatus.NEW)
                    .build());
        }
        return cart;
    }

    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    public boolean checkBoxQuantities(Cart cart) {
        if (cart == null) {
            return false;
        }
        return checkS(cart) && checkM(cart) && checkL(cart);
    }

    public boolean checkFlavorQuantities(Cart cart) {
        if (cart == null) {
            return false;
        }
        return checkFlavorSize(cart, BoxSize.S)
                && checkFlavorSize(cart, BoxSize.M)
                && checkFlavorSize(cart, BoxSize.L);
    }

    private boolean checkFlavorSize(Cart cart, BoxSize boxSize) {
        return cart.getBoxes()
                .stream()
                .filter(box -> boxSize.equals(box.getBoxSize()))
                .map(box -> box.getFlavors().size())
                .allMatch(size -> boxSize.getFlavorNumber().equals(size));
    }

    private boolean checkS(Cart cart) {
        return cart.getSNumber() == cart.getBoxes().stream().filter(box -> BoxSize.S.equals(box.getBoxSize())).count();
    }

    private boolean checkM(Cart cart) {
        return cart.getMNumber() == cart.getBoxes().stream().filter(box -> BoxSize.M.equals(box.getBoxSize())).count();
    }

    private boolean checkL(Cart cart) {
        return cart.getMNumber() == cart.getBoxes().stream().filter(box -> BoxSize.L.equals(box.getBoxSize())).count();
    }
}