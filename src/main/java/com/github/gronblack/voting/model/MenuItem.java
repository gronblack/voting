package com.github.gronblack.voting.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"actual_date", "dish_id"}, name = "menu_dish_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class MenuItem extends BaseEntity {

    @Column(name = "actual_date", nullable = false)
    @NotNull
    private LocalDate actualDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "dish_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Dish dish;

    public MenuItem(Integer id, LocalDate actualDate, Dish dish) {
        super(id);
        this.actualDate = actualDate;
        this.dish = dish;
    }
}
