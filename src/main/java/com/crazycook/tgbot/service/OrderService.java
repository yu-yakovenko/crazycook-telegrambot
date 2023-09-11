package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.DeliveryMethod;
import com.crazycook.tgbot.entity.Order;
import com.crazycook.tgbot.entity.OrderStatus;
import com.crazycook.tgbot.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.crazycook.tgbot.bot.Messages.BLUE_DIAMOND;
import static com.crazycook.tgbot.bot.Messages.BOLD_END;
import static com.crazycook.tgbot.bot.Messages.BOLD_START;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.ONE_SPACE;
import static com.crazycook.tgbot.entity.BoxSize.L;
import static com.crazycook.tgbot.entity.BoxSize.M;
import static com.crazycook.tgbot.entity.BoxSize.S;


@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final BoxService boxService;
    private final PriceService priceService;

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

    public List<Order> findAllActiveOrders() {
        return orderRepository.findByStatus(OrderStatus.IN_PROGRESS);
    }

    public Set<Box> getBoxesForOrder(Long id) {
        Optional<Order> optOrder = orderRepository.findByIdWithBoxes(id);
        if (optOrder.isPresent()) {
            Order order = optOrder.get();
            return order.getBoxes();
        } else {
            return Collections.emptySet();
        }
    }

    public BigDecimal countOverallPrice(Order order) {
        BigDecimal price = new BigDecimal(0);
        Set<Box> boxes = getBoxesForOrder(order.getId());

        BigDecimal sPrice = priceService.getBoxPrice(S);
        BigDecimal mPrice = priceService.getBoxPrice(M);
        BigDecimal lPrice = priceService.getBoxPrice(L);
        BigDecimal courierDeliveryPrice = priceService.getDeliveryPrice();

        price = price.add(sPrice.multiply(BigDecimal.valueOf(boxes.stream().filter(b -> S.equals(b.getBoxSize())).count())));
        price = price.add(mPrice.multiply(BigDecimal.valueOf(boxes.stream().filter(b -> M.equals(b.getBoxSize())).count())));
        price = price.add(lPrice.multiply(BigDecimal.valueOf(boxes.stream().filter(b -> L.equals(b.getBoxSize())).count())));

        if (DeliveryMethod.COURIER.equals(order.getDeliveryMethod())) {
            price = price.add(courierDeliveryPrice);
        }
        return price;
    }

    public String flavorMixToString(Order order) {
        Set<Box> boxes = getBoxesForOrder(order.getId());
        return flavorMixToString(boxes, S) + flavorMixToString(boxes, M) + flavorMixToString(boxes, L);
    }

    private String flavorMixToString(Set<Box> boxes, BoxSize size) {
        String flavorMixStr = "";
        long sMix = boxes.stream().filter(b -> size.equals(b.getBoxSize()) && b.getIsMix()).count();
        if (sMix > 0) {
            flavorMixStr += BLUE_DIAMOND + BOLD_START + sMix + ONE_SPACE + size + " боксів, що містять мікс смаків" + BOLD_END + LINE_END;
        }
        return flavorMixStr;
    }
}
