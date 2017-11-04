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
    private LocalDate start_Date;

    @Column(name = "end_date")
    private LocalDate end_Date;

    @Column(name = "start_time")
    private ZonedDateTime start_time;

    @Column(name = "end_time")
    private ZonedDateTime end_time;

    @NotNull
    @Column(name = "event_coordinator", nullable = false)
    private String event_Coordinator;

    @Column(name = "sponser_name")
    private String sponser_Name;

    @ManyToOne(optional = false)
    @NotNull
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

    public LocalDate getStart_Date() {
        return start_Date;
    }

    public Event start_Date(LocalDate start_Date) {
        this.start_Date = start_Date;
        return this;
    }

    public void setStart_Date(LocalDate start_Date) {
        this.start_Date = start_Date;
    }

    public LocalDate getEnd_Date() {
        return end_Date;
    }

    public Event end_Date(LocalDate end_Date) {
        this.end_Date = end_Date;
        return this;
    }

    public void setEnd_Date(LocalDate end_Date) {
        this.end_Date = end_Date;
    }

    public ZonedDateTime getStart_time() {
        return start_time;
    }

    public Event start_time(ZonedDateTime start_time) {
        this.start_time = start_time;
        return this;
    }

    public void setStart_time(ZonedDateTime start_time) {
        this.start_time = start_time;
    }

    public ZonedDateTime getEnd_time() {
        return end_time;
    }

    public Event end_time(ZonedDateTime end_time) {
        this.end_time = end_time;
        return this;
    }

    public void setEnd_time(ZonedDateTime end_time) {
        this.end_time = end_time;
    }

    public String getEvent_Coordinator() {
        return event_Coordinator;
    }

    public Event event_Coordinator(String event_Coordinator) {
        this.event_Coordinator = event_Coordinator;
        return this;
    }

    public void setEvent_Coordinator(String event_Coordinator) {
        this.event_Coordinator = event_Coordinator;
    }

    public String getSponser_Name() {
        return sponser_Name;
    }

    public Event sponser_Name(String sponser_Name) {
        this.sponser_Name = sponser_Name;
        return this;
    }

    public void setSponser_Name(String sponser_Name) {
        this.sponser_Name = sponser_Name;
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
            ", start_Date='" + getStart_Date() + "'" +
            ", end_Date='" + getEnd_Date() + "'" +
            ", start_time='" + getStart_time() + "'" +
            ", end_time='" + getEnd_time() + "'" +
            ", event_Coordinator='" + getEvent_Coordinator() + "'" +
            ", sponser_Name='" + getSponser_Name() + "'" +
            "}";
    }
}
