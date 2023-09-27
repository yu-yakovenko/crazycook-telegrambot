package com.crazycook.tgbot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlavorQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "box_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Box box;

    @ManyToOne
    @JoinColumn(name = "flavor_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Flavor flavor;

    private Integer quantity;

    @Override
    public String toString() {
        return quantity + " макаронів зі смаком " + flavor.getName() + "; \n";
    }
}
