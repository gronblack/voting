package ru.topjava.voting.to;

import org.springframework.lang.Nullable;

import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class MenuTo extends NamedTo implements Serializable {
    @Serial
    private static final long serialVersionUID = -2541687332583892431L;

    @Nullable
    private LocalDate registered;

    @PositiveOrZero
    private Integer restaurantId;

    public MenuTo() {
    }

    public MenuTo(Integer id, String name, LocalDate registered, Integer restaurantId) {
        super(id, name);
        this.registered = registered;
        this.restaurantId = restaurantId;
    }

    public LocalDate getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDate registered) {
        this.registered = registered;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }
}
