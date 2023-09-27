package com.crazycook.tgbot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "user_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_chat_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    private String comment;

    private String address;

    @ManyToOne
    @JoinColumn(name = "promo_id")
    private Promo promoCode;

    @Builder.Default
    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = {CascadeType.ALL})
    private Set<Box> boxes = new HashSet<>();
}
