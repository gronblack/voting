package com.github.gronblack.voting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishTo extends NamedTo implements Serializable {

    @Serial
    private static final long serialVersionUID = -9096320387795051113L;

    @Positive
    double price;

    @PositiveOrZero
    int restaurantId;

    public DishTo(Integer id, String name, double price, int restaurantId) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
    }
}
