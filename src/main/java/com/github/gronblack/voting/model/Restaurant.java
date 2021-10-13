package com.github.gronblack.voting.model;

import com.github.gronblack.voting.util.validation.NoHtml;
import com.github.gronblack.voting.util.validation.PhoneNumber;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "phone", nullable = false)
    @PhoneNumber
    @NoHtml
    protected String phone;

    public Restaurant(Integer id, String name, String phone) {
        super(id, name);
        this.phone = phone;
    }
}
