package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.CartStatus;
import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;


@Service
@AllArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerService customerService;
    private final BoxService boxService;

    public Cart findCart(Long chatId, String username) {
        return cartRepository.findByCustomer(customerService.createOrFind(chatId, username));
    }

    public Cart createOrFind(Long chatId, String username) {
        return createOrFind(customerService.createOrFind(chatId, username));
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

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public boolean checkBoxQuantities(Cart cart) {
        if (cart == null) {
            return false;
        }
        return checkBoxesQuantities(cart, cart.getSNumber(), BoxSize.S)
                && checkBoxesQuantities(cart, cart.getSNumber(), BoxSize.M)
                && checkBoxesQuantities(cart, cart.getSNumber(), BoxSize.L);
    }

    private boolean checkBoxesQuantities(Cart cart, int sizeNumber, BoxSize boxSize) {
        return sizeNumber == cart.getBoxes().stream().filter(box -> boxSize.equals(box.getBoxSize())).count();
    }


    public Set<Box> getBoxesForCart(Long id) {
        Optional<Cart> optCart = cartRepository.findByIdWithBoxes(id);
        if (optCart.isPresent()) {
            Cart cart = optCart.get();
            return cart.getBoxes();
        } else {
            return Collections.emptySet();
        }
    }

    public void createNewBoxInProgress(Cart cart, BoxSize nextBoxSize) {
        Box newBox = Box.builder()
                .boxSize(nextBoxSize)
                .cart(cart)
                .build();
        cart.setBoxInProgress(boxService.save(newBox));
        save(cart);
    }

    public int findCurrentBoxIndex(Cart cart, BoxSize boxSize) {
        Set<Box> boxes = getBoxesForCart(cart.getId());
        long thisSizeBoxesNumber = boxes.stream().filter(b -> boxSize.equals(b.getBoxSize())).count();
        return (int) thisSizeBoxesNumber;
    }

    public boolean isMoreBoxesPossible(Cart cart) {
        int allBoxesNumber = cart.getSNumber() + cart.getMNumber() + cart.getLNumber();
        Set<Box> boxes = getBoxesForCart(cart.getId());
        return allBoxesNumber > boxes.size();
    }

    public Cart completeBoxInProgress(Cart cart) {
        Set<Box> boxes = getBoxesForCart(cart.getId());
        boxes.add(cart.getBoxInProgress());
        cart.setBoxes(boxes);
        cart.setBoxInProgress(null);
        return save(cart);
    }
}
