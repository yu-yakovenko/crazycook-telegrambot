package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.Order;
import com.crazycook.tgbot.entity.OrderStatus;
import com.crazycook.tgbot.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final BoxService boxService;

    public Order createOrder(Cart cart) {
        Set<Box> boxes = cartService.getBoxesForCart(cart.getId());
        Order order = Order.builder()
                .customer(cart.getCustomer())
                .boxes(cartService.getBoxesForCart(cart.getId()))
                .comment(cart.getComment())
                .address(cart.getAddress())
                .deliveryMethod(cart.getDeliveryMethod())
                .promoCode(cart.getPromoCode())
                .status(OrderStatus.IN_PROGRESS)
                .build();
        order = orderRepository.save(order);

        for (Box box : boxes) {
            box.setOrder(order);
            boxService.save(box);
        }

        return order;
    }
}
