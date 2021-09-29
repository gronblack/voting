package com.github.gronblack.voting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.lang.Nullable;

import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuTo extends NamedTo implements Serializable {

    @Serial
    private static final long serialVersionUID = -2541687332583892431L;

    @Nullable
    @NonFinal
    LocalDate registered;

    @PositiveOrZero
    Integer restaurantId;

    public MenuTo(Integer id, String name, @Nullable LocalDate registered, Integer restaurantId) {
        super(id, name);
        this.registered = registered;
        this.restaurantId = restaurantId;
    }

    public void setRegistered(@Nullable LocalDate registered) {
        this.registered = registered;
    }
}
