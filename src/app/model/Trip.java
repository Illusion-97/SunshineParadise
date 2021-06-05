package app.model;

import java.io.Serializable;

public class Trip implements Serializable,Bean {
    private Long id;
    private String name;
    private final Long departure;
    private final Long terminus;
    private final float cost;

    public Trip(Long id, String name, Long departure, Long terminus, float cost) {
        this.id = id;
        this.name = name;
        this.departure = departure;
        this.terminus = terminus;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDeparture() {
        return departure;
    }

    public long getTerminus() {
        return terminus;
    }

    public float getCost() {
        return cost;
    }
}
