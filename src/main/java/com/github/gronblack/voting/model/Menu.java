package com.github.gronblack.voting.model;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "restaurant_id"}, name = "menu_restaurant_idx")})
public class Menu extends NamedEntity {
    @Column(name = "registered", nullable = false)
    private LocalDate registered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToMany
    @JoinTable(name = "menu_dish",
            joinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id", referencedColumnName = "id")
    )
    private Set<Dish> dishes;

    public Menu() {
    }

    public Menu(String name, LocalDate registered, Restaurant restaurant) {
        this(null, name, registered, restaurant, null);
    }

    public Menu(String name, LocalDate registered, Restaurant restaurant, Dish... dishes) {
        this(null, name, registered, restaurant, Arrays.asList(dishes));
    }

    public Menu(Integer id, String name, LocalDate registered, Restaurant restaurant) {
        this(id, name, registered, restaurant, null);
    }

    public Menu(Menu m) {
        this(m.getId(), m.getName(), m.getRegistered(), m.getRestaurant(), m.getDishes());
    }

    public Menu(Integer id, String name, LocalDate registered, Restaurant restaurant, Collection<Dish> dishes) {
        super(id, name);
        this.registered = registered;
        this.restaurant = restaurant;
        setDishes(dishes);
    }

    public LocalDate getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDate date) {
        this.registered = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Set<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(Collection<Dish> dishes) {
        this.dishes = CollectionUtils.isEmpty(dishes) ? Collections.emptySet() : new HashSet<>(dishes);
    }

    public void addDishes(Dish... dishes) {
        this.dishes.addAll(Arrays.asList(dishes));
    }

    public void removeDishes(Dish... dishes) {
        Arrays.asList(dishes).forEach(this.dishes::remove);
    }

    public Menu clearDishes() {
        dishes.clear();
        return this;
    }
}
