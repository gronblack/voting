package ru.topjava.voting.to;

import java.io.Serial;
import java.io.Serializable;

public class RestaurantToDelete extends NamedTo implements Serializable {
    @Serial
    private static final long serialVersionUID = 5581350404739469341L;

    private long rating;

    public RestaurantToDelete() {
    }

    public RestaurantToDelete(Integer id, String name, long rating) {
        super(id, name);
        this.rating = rating;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
