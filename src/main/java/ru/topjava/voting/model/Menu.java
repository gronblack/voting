package ru.topjava.voting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "restaurant_id"}, name = "menu_restaurant_idx")})
public class Menu extends NamedEntity {
    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

//    @ManyToMany
//    private List<Dish> dishes;

    public Menu() {
    }

    public Menu(LocalDate date, Restaurant restaurant) {
        this(null, null, date, restaurant, null);
    }

    public Menu(String name, LocalDate date, Restaurant restaurant, Dish... dishes) {
        this(null, name, date, restaurant, Arrays.asList(dishes));
    }

    public Menu(Integer id, String name, LocalDate date, Restaurant restaurant, Collection<Dish> dishes) {
        super(id, name);
        this.date = date;
        this.restaurant = restaurant;
        //setDishes(dishes);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

//    public List<Dish> getDishes() {
//        return dishes;
//    }
//
//    public void setDishes(Collection<Dish> dishes) {
//        this.dishes = CollectionUtils.isEmpty(dishes) ? Collections.<Dish>emptyList() : List.copyOf(dishes);
//    }
}
