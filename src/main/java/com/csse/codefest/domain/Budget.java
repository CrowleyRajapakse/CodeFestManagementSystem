package com.csse.codefest.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Budget.
 */
@Entity
@Table(name = "budget")
@Document(indexName = "budget")
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food")
    private Double food;

    @Column(name = "decorations")
    private Double decorations;

    @Column(name = "prizes")
    private Double prizes;

    @Column(name = "photography")
    private Double photography;

    @Column(name = "transport")
    private Double transport;

    @Column(name = "stationery")
    private Double stationery;

    @Column(name = "guests")
    private Double guests;

    @Column(name = "other")
    private Double other;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Event event;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFood() {
        return food;
    }

    public Budget food(Double food) {
        this.food = food;
        return this;
    }

    public void setFood(Double food) {
        this.food = food;
    }

    public Double getDecorations() {
        return decorations;
    }

    public Budget decorations(Double decorations) {
        this.decorations = decorations;
        return this;
    }

    public void setDecorations(Double decorations) {
        this.decorations = decorations;
    }

    public Double getPrizes() {
        return prizes;
    }

    public Budget prizes(Double prizes) {
        this.prizes = prizes;
        return this;
    }

    public void setPrizes(Double prizes) {
        this.prizes = prizes;
    }

    public Double getPhotography() {
        return photography;
    }

    public Budget photography(Double photography) {
        this.photography = photography;
        return this;
    }

    public void setPhotography(Double photography) {
        this.photography = photography;
    }

    public Double getTransport() {
        return transport;
    }

    public Budget transport(Double transport) {
        this.transport = transport;
        return this;
    }

    public void setTransport(Double transport) {
        this.transport = transport;
    }

    public Double getStationery() {
        return stationery;
    }

    public Budget stationery(Double stationery) {
        this.stationery = stationery;
        return this;
    }

    public void setStationery(Double stationery) {
        this.stationery = stationery;
    }

    public Double getGuests() {
        return guests;
    }

    public Budget guests(Double guests) {
        this.guests = guests;
        return this;
    }

    public void setGuests(Double guests) {
        this.guests = guests;
    }

    public Double getOther() {
        return other;
    }

    public Budget other(Double other) {
        this.other = other;
        return this;
    }

    public void setOther(Double other) {
        this.other = other;
    }

    public Event getEvent() {
        return event;
    }

    public Budget event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Budget budget = (Budget) o;
        if (budget.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), budget.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Budget{" +
            "id=" + getId() +
            ", food='" + getFood() + "'" +
            ", decorations='" + getDecorations() + "'" +
            ", prizes='" + getPrizes() + "'" +
            ", photography='" + getPhotography() + "'" +
            ", transport='" + getTransport() + "'" +
            ", stationery='" + getStationery() + "'" +
            ", guests='" + getGuests() + "'" +
            ", other='" + getOther() + "'" +
            "}";
    }
}
