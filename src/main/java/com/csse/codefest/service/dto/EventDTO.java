package com.csse.codefest.service.dto;


import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Event entity.
 */
public class EventDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String venue;

    private LocalDate startDate;

    private LocalDate endDate;

    private ZonedDateTime etime;

    @NotNull
    private String ecoordinator;

    private String teamCompetitorName;

    private Long competitionId;

    private String competitionTitle;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getEtime() {
        return etime;
    }

    public void setEtime(ZonedDateTime etime) {
        this.etime = etime;
    }

    public String getEcoordinator() {
        return ecoordinator;
    }

    public void setEcoordinator(String ecoordinator) {
        this.ecoordinator = ecoordinator;
    }

    public String getTeamCompetitorName() {
        return teamCompetitorName;
    }

    public void setTeamCompetitorName(String teamCompetitorName) {
        this.teamCompetitorName = teamCompetitorName;
    }

    public Long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    public String getCompetitionTitle() {
        return competitionTitle;
    }

    public void setCompetitionTitle(String competitionTitle) {
        this.competitionTitle = competitionTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;
        if(eventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EventDTO{" +
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
