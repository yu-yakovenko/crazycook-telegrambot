package com.crazycook.tgbot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @Builder.Default
    @Column(name = "s_number")
    private int sNumber = 0;

    @Builder.Default
    @Column(name = "m_number")
    private int mNumber = 0;

    @Builder.Default
    @Column(name = "l_number")
    private int lNumber = 0;

    @OneToOne
    @JoinColumn(name = "customer_chat_id", referencedColumnName = "chatId")
    private Customer customer;

    @Builder.Default
    @OneToMany(mappedBy = "cart")
    private List<Box> boxes = new ArrayList<>();

}
