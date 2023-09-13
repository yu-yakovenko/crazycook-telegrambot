package com.crazycook.tgbot.service;

import com.crazycook.tgbot.entity.Box;
import com.crazycook.tgbot.entity.BoxSize;
import com.crazycook.tgbot.entity.Cart;
import com.crazycook.tgbot.entity.CartStatus;
import com.crazycook.tgbot.entity.Customer;
import com.crazycook.tgbot.entity.DeliveryMethod;
import com.crazycook.tgbot.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerService customerService;
    private final BoxService boxService;
    private final PriceService priceService;

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
                    .status(CartStatus.IN_PROGRESS)
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
        return checkBoxesQuantities(cart, cart.getSNumber(), S)
                && checkBoxesQuantities(cart, cart.getSNumber(), M)
                && checkBoxesQuantities(cart, cart.getSNumber(), L);
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

    public String flavorMixToString(Cart cart) {
        Set<Box> boxes = getBoxesForCart(cart.getId());
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

    public void delete(Cart cart) {
        cart.setStatus(CartStatus.IN_PROGRESS);
        cart.setSNumber(0);
        cart.setMNumber(0);
        cart.setLNumber(0);
        cart.setBoxInProgress(null);
        cart.setCurrentFlavor(null);
        cart.setDeliveryMethod(null);
        cart.setComment(null);
        cart.setBoxes(null);
        cart.setAddress(null);
        cart.setPromoCode(null);
        getBoxesForCart(cart.getId()).forEach(box -> {
            box.setCart(null);
            boxService.save(box);
        });
        cartRepository.save(cart);
    }

    public BigDecimal countOverallPrice(Cart cart) {
        BigDecimal price = new BigDecimal(0);
        Set<Box> boxes = getBoxesForCart(cart.getId());

        BigDecimal sPrice = priceService.getBoxPrice(S);
        BigDecimal mPrice = priceService.getBoxPrice(M);
        BigDecimal lPrice = priceService.getBoxPrice(L);
        BigDecimal courierDeliveryPrice = priceService.getDeliveryPrice();

        price = price.add(sPrice.multiply(BigDecimal.valueOf(boxes.stream().filter(b -> S.equals(b.getBoxSize())).count())));
        price = price.add(mPrice.multiply(BigDecimal.valueOf(boxes.stream().filter(b -> M.equals(b.getBoxSize())).count())));
        price = price.add(lPrice.multiply(BigDecimal.valueOf(boxes.stream().filter(b -> L.equals(b.getBoxSize())).count())));

        if (DeliveryMethod.COURIER.equals(cart.getDeliveryMethod())) {
            price = price.add(courierDeliveryPrice);
        }
        return price;
    }

    public String cartBoxesToString(Cart cart) {
        Set<Box> boxes = getBoxesForCart(cart.getId());
        StringBuilder sb = new StringBuilder();

        int filledSNumber = getBoxNumber(boxes, BoxSize.S);
        int filledMNumber = getBoxNumber(boxes, BoxSize.M);
        int filledLNumber = getBoxNumber(boxes, BoxSize.L);

        sb.append(boxService.messageForEmptyBoxes(cart, filledSNumber, filledMNumber, filledLNumber));

        sb.append(flavorMixToString(cart));
        List<String> flavorDescription = boxes.stream().map(boxService::customFlavorToString).collect(Collectors.toList());

        for (String s : flavorDescription) {
            sb.append(s);
        }
        return sb.toString();
    }

    private int getBoxNumber(Set<Box> boxes, BoxSize size) {
        return (int) boxes.stream().filter(b -> size.equals(b.getBoxSize())).count();
    }

    public boolean containsEmptyBoxes(Cart cart) {
        Set<Box> boxes = getBoxesForCart(cart.getId());
        int filledSNumber = getBoxNumber(boxes, BoxSize.S);
        int filledMNumber = getBoxNumber(boxes, BoxSize.M);
        int filledLNumber = getBoxNumber(boxes, BoxSize.L);
        return filledSNumber < cart.getSNumber() || filledMNumber < cart.getMNumber() || filledLNumber < cart.getLNumber();
    }
}
