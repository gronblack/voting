package ru.topjava.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant")
public class Restaurant extends NamedEntity {
    @OneToMany(mappedBy = "restaurant")
    //@OrderBy("date DESC")
    //@JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Menu> menus;

    @OneToMany(mappedBy = "restaurant")
    @OrderBy("name DESC")
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Dish> dishes;

    @OneToMany(mappedBy = "restaurant")
    //@OrderBy("date DESC")
    //@JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Vote> votes;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        this(id, name, null);
    }

    public Restaurant(Integer id, String name, Set<Dish> dishes) {
        super(id, name);
        this.dishes = dishes;
    }

    //    public List<Menu> getMenus() {
//        return menus;
//    }
//
//    public void setMenus(List<Menu> menus) {
//        this.menus = menus;
//    }
//
    public Set<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }
}
