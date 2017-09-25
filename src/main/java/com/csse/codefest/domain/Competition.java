package com.csse.codefest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Competition.
 */
@Entity
@Table(name = "competition")
public class Competition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "theme")
    private String theme;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "sponsor")
    private String sponsor;

    @Column(name = "objective")
    private String objective;

    @NotNull
    @Column(name = "coordinator", nullable = false)
    private String coordinator;

    @Column(name = "about_us")
    private String aboutUs;

    @OneToMany(mappedBy = "competition")
    @JsonIgnore
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "competition")
    @JsonIgnore
    private Set<Workshop> workshops = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Competition title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTheme() {
        return theme;
    }

    public Competition theme(String theme) {
        this.theme = theme;
        return this;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public Competition description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public Competition venue(String venue) {
        this.venue = venue;
        return this;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Competition startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Competition endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getSponsor() {
        return sponsor;
    }

    public Competition sponsor(String sponsor) {
        this.sponsor = sponsor;
        return this;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getObjective() {
        return objective;
    }

    public Competition objective(String objective) {
        this.objective = objective;
        return this;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getCoordinator() {
        return coordinator;
    }

    public Competition coordinator(String coordinator) {
        this.coordinator = coordinator;
        return this;
    }

    public void setCoordinator(String coordinator) {
        this.coordinator = coordinator;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public Competition aboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
        return this;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Competition events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Competition addEvent(Event event) {
        this.events.add(event);
        event.setCompetition(this);
        return this;
    }

    public Competition removeEvent(Event event) {
        this.events.remove(event);
        event.setCompetition(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Workshop> getWorkshops() {
        return workshops;
    }

    public Competition workshops(Set<Workshop> workshops) {
        this.workshops = workshops;
        return this;
    }

    public Competition addWorkshop(Workshop workshop) {
        this.workshops.add(workshop);
        workshop.setCompetition(this);
        return this;
    }

    public Competition removeWorkshop(Workshop workshop) {
        this.workshops.remove(workshop);
        workshop.setCompetition(null);
        return this;
    }

    public void setWorkshops(Set<Workshop> workshops) {
        this.workshops = workshops;
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
        Competition competition = (Competition) o;
        if (competition.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), competition.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Competition{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", theme='" + getTheme() + "'" +
            ", description='" + getDescription() + "'" +
            ", venue='" + getVenue() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", sponsor='" + getSponsor() + "'" +
            ", objective='" + getObjective() + "'" +
            ", coordinator='" + getCoordinator() + "'" +
            ", aboutUs='" + getAboutUs() + "'" +
            "}";
    }
}
