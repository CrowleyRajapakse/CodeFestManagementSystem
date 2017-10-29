package com.csse.codefest.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Document(indexName = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "etime")
    private ZonedDateTime etime;

    @NotNull
    @Column(name = "ecoordinator", nullable = false)
    private String ecoordinator;

    @Column(name = "team_competitor_name")
    private String teamCompetitorName;

    @ManyToOne
    private Competition competition;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Event name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Event description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public Event venue(String venue) {
        this.venue = venue;
        return this;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Event startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Event endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getEtime() {
        return etime;
    }

    public Event etime(ZonedDateTime etime) {
        this.etime = etime;
        return this;
    }

    public void setEtime(ZonedDateTime etime) {
        this.etime = etime;
    }

    public String getEcoordinator() {
        return ecoordinator;
    }

    public Event ecoordinator(String ecoordinator) {
        this.ecoordinator = ecoordinator;
        return this;
    }

    public void setEcoordinator(String ecoordinator) {
        this.ecoordinator = ecoordinator;
    }

    public String getTeamCompetitorName() {
        return teamCompetitorName;
    }

    public Event teamCompetitorName(String teamCompetitorName) {
        this.teamCompetitorName = teamCompetitorName;
        return this;
    }

    public void setTeamCompetitorName(String teamCompetitorName) {
        this.teamCompetitorName = teamCompetitorName;
    }

    public Competition getCompetition() {
        return competition;
    }

    public Event competition(Competition competition) {
        this.competition = competition;
        return this;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
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
        Event event = (Event) o;
        if (event.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", venue='" + getVenue() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", etime='" + getEtime() + "'" +
            ", ecoordinator='" + getEcoordinator() + "'" +
            ", teamCompetitorName='" + getTeamCompetitorName() + "'" +
            "}";
    }
}
