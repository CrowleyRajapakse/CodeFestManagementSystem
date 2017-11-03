package com.csse.codefest.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Judges.
 */
@Entity
@Table(name = "judges")
@Document(indexName = "judges")
public class Judges implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "job_title")
    private String job_title;

    @Column(name = "employer")
    private String employer;

    @ManyToOne(optional = false)
    @NotNull
    private Event events;

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

    public Judges name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public Judges gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public Judges email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Judges phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJob_title() {
        return job_title;
    }

    public Judges job_title(String job_title) {
        this.job_title = job_title;
        return this;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getEmployer() {
        return employer;
    }

    public Judges employer(String employer) {
        this.employer = employer;
        return this;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public Event getEvents() {
        return events;
    }

    public Judges events(Event event) {
        this.events = event;
        return this;
    }

    public void setEvents(Event event) {
        this.events = event;
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
        Judges judges = (Judges) o;
        if (judges.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), judges.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Judges{" +
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
