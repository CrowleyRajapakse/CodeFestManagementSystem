package com.csse.codefest.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Workshop entity.
 */
public class WorkshopDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String description;

    private String venue;

    @NotNull
    private String wcoordinator;

    private LocalDate swTime;

    private LocalDate swDate;

    private Long competitionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getWcoordinator() {
        return wcoordinator;
    }

    public void setWcoordinator(String wcoordinator) {
        this.wcoordinator = wcoordinator;
    }

    public LocalDate getSwTime() {
        return swTime;
    }

    public void setSwTime(LocalDate swTime) {
        this.swTime = swTime;
    }

    public LocalDate getSwDate() {
        return swDate;
    }

    public void setSwDate(LocalDate swDate) {
        this.swDate = swDate;
    }

    public Long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkshopDTO workshopDTO = (WorkshopDTO) o;
        if(workshopDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workshopDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkshopDTO{" +
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
