package com.csse.codefest.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Judges entity.
 */
public class JudgesDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String gender;

    private String email;

    private String phone;

    private String job_title;

    private String employer;

    private Long eventsId;

    private String eventsName;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public Long getEventsId() {
        return eventsId;
    }

    public void setEventsId(Long eventId) {
        this.eventsId = eventId;
    }

    public String getEventsName() {
        return eventsName;
    }

    public void setEventsName(String eventName) {
        this.eventsName = eventName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JudgesDTO judgesDTO = (JudgesDTO) o;
        if(judgesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), judgesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JudgesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gender='" + getGender() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", job_title='" + getJob_title() + "'" +
            ", employer='" + getEmployer() + "'" +
            "}";
    }
}
