package ru.topjava.voting.to;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;

public class DishTo extends NamedTo implements Serializable {
    @Serial
    private static final long serialVersionUID = -9096320387795051113L;

    @Positive
    private double price;

    @PositiveOrZero
    private Integer restaurant_id;

    public DishTo() {
    }

    public DishTo(String name, double price, int restaurant_id) {
        this(null, name, price, restaurant_id);
    }

    public DishTo(Integer id, String name, double price, int restaurant_id) {
        super(id, name);
        this.price = price;
        this.restaurant_id = restaurant_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }
}
