package com.crazycook.tgbot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "box", orphanRemoval = true, cascade = {CascadeType.ALL})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<FlavorQuantity> flavorQuantities = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private BoxSize boxSize;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Builder.Default
    private Boolean isMix = false;

    @Override
    public int hashCode() {
        return Objects.hash(id, flavorQuantities, boxSize, isMix);
    }
}
