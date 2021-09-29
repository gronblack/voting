package com.github.gronblack.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @OneToMany(mappedBy = "restaurant")
    @OrderBy("registered DESC")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Menu> menu;

    @OneToMany(mappedBy = "restaurant")
    @OrderBy("name DESC")
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Dish> dishes;

    @OneToMany(mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Vote> votes;

    public Restaurant(Integer id, String name, Set<Dish> dishes) {
        super(id, name);
        this.dishes = dishes;
    }
}
