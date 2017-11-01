package com.csse.codefest.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Resultevent entity.
 */
public class ResulteventDTO implements Serializable {

    private Long id;

    @NotNull
    private String winner;

    @NotNull
    private String runner_up1;

    @NotNull
    private String runner_up2;

    private String merit1;

    private String merit2;

    private String description;

    private Long eventresultId;

    private String eventresultName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getRunner_up1() {
        return runner_up1;
    }

    public void setRunner_up1(String runner_up1) {
        this.runner_up1 = runner_up1;
    }

    public String getRunner_up2() {
        return runner_up2;
    }

    public void setRunner_up2(String runner_up2) {
        this.runner_up2 = runner_up2;
    }

    public String getMerit1() {
        return merit1;
    }

    public void setMerit1(String merit1) {
        this.merit1 = merit1;
    }

    public String getMerit2() {
        return merit2;
    }

    public void setMerit2(String merit2) {
        this.merit2 = merit2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEventresultId() {
        return eventresultId;
    }

    public void setEventresultId(Long eventId) {
        this.eventresultId = eventId;
    }

    public String getEventresultName() {
        return eventresultName;
    }

    public void setEventresultName(String eventName) {
        this.eventresultName = eventName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResulteventDTO resulteventDTO = (ResulteventDTO) o;
        if(resulteventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resulteventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResulteventDTO{" +
            "id=" + getId() +
            ", winner='" + getWinner() + "'" +
            ", runner_up1='" + getRunner_up1() + "'" +
            ", runner_up2='" + getRunner_up2() + "'" +
            ", merit1='" + getMerit1() + "'" +
            ", merit2='" + getMerit2() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
