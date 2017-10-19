package com.csse.codefest.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Workshop.
 */
@Entity
@Table(name = "workshop")
@Document(indexName = "workshop")
public class Workshop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "venue")
    private String venue;

    @NotNull
    @Column(name = "wcoordinator", nullable = false)
    private String wcoordinator;

    @Column(name = "sw_time")
    private LocalDate swTime;

    @Column(name = "sw_date")
    private LocalDate swDate;

    @ManyToOne
    private Competition competition;

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

    public Workshop title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Workshop description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public Workshop venue(String venue) {
        this.venue = venue;
        return this;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getWcoordinator() {
        return wcoordinator;
    }

    public Workshop wcoordinator(String wcoordinator) {
        this.wcoordinator = wcoordinator;
        return this;
    }

    public void setWcoordinator(String wcoordinator) {
        this.wcoordinator = wcoordinator;
    }

    public LocalDate getSwTime() {
        return swTime;
    }

    public Workshop swTime(LocalDate swTime) {
        this.swTime = swTime;
        return this;
    }

    public void setSwTime(LocalDate swTime) {
        this.swTime = swTime;
    }

    public LocalDate getSwDate() {
        return swDate;
    }

    public Workshop swDate(LocalDate swDate) {
        this.swDate = swDate;
        return this;
    }

    public void setSwDate(LocalDate swDate) {
        this.swDate = swDate;
    }

    public Competition getCompetition() {
        return competition;
    }

    public Workshop competition(Competition competition) {
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
        Workshop workshop = (Workshop) o;
        if (workshop.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workshop.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Workshop{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", venue='" + getVenue() + "'" +
            ", wcoordinator='" + getWcoordinator() + "'" +
            ", swTime='" + getSwTime() + "'" +
            ", swDate='" + getSwDate() + "'" +
            "}";
    }
}
