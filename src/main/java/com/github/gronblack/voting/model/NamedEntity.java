package com.github.gronblack.voting.model;

import com.github.gronblack.voting.util.validation.NoHtml;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class NamedEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(max = 100)
    @NoHtml
    protected String name;

    protected NamedEntity() {
    }

    protected NamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}
