package com.github.gronblack.voting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.lang.Nullable;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuItemTo extends BaseTo {

    @NonFinal
    @Nullable
    LocalDate actualDate;

    @PositiveOrZero
    int dishId;

    @PositiveOrZero
    int restaurantId;

    public MenuItemTo(Integer id, @Nullable LocalDate actualDate, int restaurantId, int dishId) {
        super(id);
        this.actualDate = actualDate;
        this.restaurantId = restaurantId;
        this.dishId = dishId;
    }

    public void setActualDate(@Nullable LocalDate actualDate) {
        this.actualDate = actualDate;
    }
}
