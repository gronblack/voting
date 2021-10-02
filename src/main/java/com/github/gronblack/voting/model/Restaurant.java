package com.github.gronblack.voting.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}
