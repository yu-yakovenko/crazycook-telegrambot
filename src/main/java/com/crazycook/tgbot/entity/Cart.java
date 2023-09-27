package com.crazycook.tgbot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    @Builder.Default
    @Column(name = "s_number")
    private int sNumber = 0;

    @Builder.Default
    @Column(name = "m_number")
    private int mNumber = 0;

    @Builder.Default
    @Column(name = "l_number")
    private int lNumber = 0;

    private String comment;

    private String address;

    @ManyToOne
    @JoinColumn(name = "promo_id")
    private Promo promoCode;

    @OneToOne
    @JoinColumn(name = "box_in_progress_id", referencedColumnName = "id")
    private Box boxInProgress;

    @OneToOne
    @JoinColumn(name = "current_flavor_id", referencedColumnName = "id")
    private Flavor currentFlavor;

    @OneToOne
    @JoinColumn(name = "customer_chat_id", referencedColumnName = "chatId")
    private Customer customer;

    @Builder.Default
    @OneToMany(mappedBy = "cart", orphanRemoval = true, cascade = {CascadeType.ALL})
    private Set<Box> boxes = new HashSet<>();

}
