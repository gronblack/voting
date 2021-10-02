package com.github.gronblack.voting.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"actual_date", "user_id"}, name = "vote_date_user_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Vote extends BaseEntity {

    @Column(name = "actual_date", nullable = false)
    @NotNull
    private LocalDate actualDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    public Vote(LocalDate actualDate, User user) {
        this(null, actualDate, user, null);
    }

    public Vote(Integer id, LocalDate actualDate, User user, Restaurant restaurant) {
        super(id);
        this.actualDate = actualDate;
        this.user = user;
        this.restaurant = restaurant;
    }
}
