package com.crazycook.tgbot.service;

import com.crazycook.tgbot.bot.Messages;
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

import static com.crazycook.tgbot.bot.Messages.ADDRESS;
import static com.crazycook.tgbot.bot.Messages.BOLD_END;
import static com.crazycook.tgbot.bot.Messages.BOLD_START;
import static com.crazycook.tgbot.bot.Messages.COMMENT_IS;
import static com.crazycook.tgbot.bot.Messages.DELIVERY_METHOD;
import static com.crazycook.tgbot.bot.Messages.LINE_END;
import static com.crazycook.tgbot.bot.Messages.OVERALL_PRICE;
import static com.crazycook.tgbot.bot.Messages.PRICE_WITH_PROMO;
import static com.crazycook.tgbot.bot.Messages.YOUR_CART_IS_EMPTY;
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
    private final PromoService promoService;

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
            flavorMixStr += Messages.flavorMixStr((int) sMix, size.toString());
        }
        return flavorMixStr;
    }

    public void refresh(Long cartId) {
        Cart cart = cartRepository.getById(cartId);
        cart.setStatus(CartStatus.IN_PROGRESS);
        cart.setSNumber(0);
        cart.setMNumber(0);
        cart.setLNumber(0);
        cart.setBoxInProgress(null);
        cart.setCurrentFlavor(null);
        cart.setDeliveryMethod(null);
        cart.setComment(null);
        cart.setAddress(null);
        cart.setPromoCode(null);
        cartRepository.save(cart);

        Set<Box> boxes = getBoxesForCart(cart.getId());
        for (Box box : boxes) {
            box.setCart(null);
            boxService.save(box);
        }
    }

    public BigDecimal countOverallPriceBeforePromo(Cart cart) {
        Set<Box> boxes = getBoxesForCart(cart.getId());
        BigDecimal price = countBoxesPrice(boxes);
        BigDecimal courierDeliveryPrice = priceService.getDeliveryPrice();

        if (DeliveryMethod.COURIER.equals(cart.getDeliveryMethod())) {
            price = price.add(courierDeliveryPrice);
        }
        return price;
    }

    public BigDecimal countBoxesPrice(Set<Box> boxes) {
        BigDecimal price = new BigDecimal(0);

        BigDecimal sPrice = priceService.getBoxPrice(S);
        BigDecimal mPrice = priceService.getBoxPrice(M);
        BigDecimal lPrice = priceService.getBoxPrice(L);

        price = price.add(sPrice.multiply(BigDecimal.valueOf(boxes.stream().filter(b -> S.equals(b.getBoxSize())).count())));
        price = price.add(mPrice.multiply(BigDecimal.valueOf(boxes.stream().filter(b -> M.equals(b.getBoxSize())).count())));
        price = price.add(lPrice.multiply(BigDecimal.valueOf(boxes.stream().filter(b -> L.equals(b.getBoxSize())).count())));

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

        if (cart.getSNumber() + cart.getMNumber() + cart.getLNumber() == 0) {
            sb.append(YOUR_CART_IS_EMPTY);
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

    public boolean readyForComplete(Cart cart) {
        Set<Box> boxes = getBoxesForCart(cart.getId());
        int filledNumber = boxes.size();

        boolean allBoxFilled = filledNumber == cart.getSNumber() + cart.getMNumber() + cart.getLNumber();

        boolean deliveryFilled =
                cart.getDeliveryMethod() != null
                        && (cart.getDeliveryMethod().equals(DeliveryMethod.SELF_PICKUP)
                        || (cart.getDeliveryMethod().equals(DeliveryMethod.COURIER) && cart.getAddress() != null));
        return allBoxFilled && deliveryFilled;
    }

    public String cartSummery(Cart cart) {
        Set<Box> boxes = getBoxesForCart(cart.getId());
        StringBuilder cartSummery = new StringBuilder();

        cartSummery.append(cartBoxesToString(cart));

        cartSummery.append(delivery(cart));
        cartSummery.append(address(cart));
        cartSummery.append(comment(cart));
        cartSummery.append(price(cart));
        cartSummery.append(promo(cart, boxes));

        return cartSummery.toString();
    }

    private String delivery(Cart cart) {
        if (cart.getDeliveryMethod() != null) {
            return LINE_END + BOLD_START + DELIVERY_METHOD + BOLD_END + cart.getDeliveryMethod().getName();
        }
        return "";
    }

    private String address(Cart cart) {
        if (cart.getAddress() != null && !cart.getAddress().isBlank()) {
            return LINE_END + ADDRESS + cart.getAddress();
        }
        return "";
    }

    private String comment(Cart cart) {
        if (cart.getComment() != null && !cart.getComment().isBlank()) {
            return LINE_END + BOLD_START + COMMENT_IS
                    + BOLD_END + cart.getComment();
        }
        return "";
    }

    private String promo(Cart cart, Set<Box> boxes) {
        BigDecimal boxesPrice = countBoxesPrice(boxes);

        if (cart.getPromoCode() != null && boxesPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal promoPrice = promoService.countPromoPrice(boxesPrice, cart.getPromoCode());
            if (cart.getDeliveryMethod() != null && DeliveryMethod.COURIER.equals(cart.getDeliveryMethod())) {
                promoPrice = promoPrice.add(priceService.getDeliveryPrice());
            }
            return LINE_END + String.format(PRICE_WITH_PROMO, cart.getPromoCode().getName(), promoPrice);
        }
        return "";
    }

    private String price(Cart cart) {
        if (cart.getSNumber() + cart.getMNumber() + cart.getLNumber() > 0) {
            return LINE_END + String.format(OVERALL_PRICE, countOverallPriceBeforePromo(cart)) + LINE_END;
        }
        return "";
    }
}
