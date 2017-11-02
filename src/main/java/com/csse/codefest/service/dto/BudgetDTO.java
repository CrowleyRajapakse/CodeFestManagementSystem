package com.csse.codefest.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Budget entity.
 */
public class BudgetDTO implements Serializable {

    private Long id;

    private Double food;

    private Double decorations;

    private Double prizes;

    private Double photography;

    private Double transport;

    private Double stationery;

    private Double guests;

    private Double other;

    private Long eventId;

    private String eventName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFood() {
        return food;
    }

    public void setFood(Double food) {
        this.food = food;
    }

    public Double getDecorations() {
        return decorations;
    }

    public void setDecorations(Double decorations) {
        this.decorations = decorations;
    }

    public Double getPrizes() {
        return prizes;
    }

    public void setPrizes(Double prizes) {
        this.prizes = prizes;
    }

    public Double getPhotography() {
        return photography;
    }

    public void setPhotography(Double photography) {
        this.photography = photography;
    }

    public Double getTransport() {
        return transport;
    }

    public void setTransport(Double transport) {
        this.transport = transport;
    }

    public Double getStationery() {
        return stationery;
    }

    public void setStationery(Double stationery) {
        this.stationery = stationery;
    }

    public Double getGuests() {
        return guests;
    }

    public void setGuests(Double guests) {
        this.guests = guests;
    }

    public Double getOther() {
        return other;
    }

    public void setOther(Double other) {
        this.other = other;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BudgetDTO budgetDTO = (BudgetDTO) o;
        if(budgetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), budgetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BudgetDTO{" +
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
